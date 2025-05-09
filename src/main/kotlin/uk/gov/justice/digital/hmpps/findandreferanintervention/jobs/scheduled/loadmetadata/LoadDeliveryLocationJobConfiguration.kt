package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata

import mu.KLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.TimestampIncrementer
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryLocationRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PduRefRepository
import java.util.*

data class BatchDeliveryLocation(
  val id: String,
  val providerName: String = "",
  val pduRef: String,
  val status: String,
)

@Configuration
@EnableTransactionManagement
class LoadDeliveryLocationJobConfiguration(
  @Qualifier("jobRepository") private val jobRepository: JobRepository,
  private val deliveryLocationRepository: DeliveryLocationRepository,
  private val interventionCatalogueRepository: InterventionCatalogueRepository,
  private val pduRefRepository: PduRefRepository,
  private val transactionManager: PlatformTransactionManager,
) {
  companion object : KLogging()

  @Bean
  fun loadDeliveryLocationJob(): Job = JobBuilder("loadDeliveryLocationJob", jobRepository)
    .incrementer(TimestampIncrementer())
    .start(deliveryLocationStep())
    .build()

  @Bean
  fun deliveryLocationReader(): ItemReader<BatchDeliveryLocation> = FlatFileItemReaderBuilder<BatchDeliveryLocation>()
    .name("deliveryLocationReader")
    .resource(ClassPathResource("csv/delivery_location.csv"))
    .linesToSkip(1)
    .delimited()
    .names("intervention_id", "provider_name", "pdu_ref_id", "status")
    .fieldSetMapper { fieldSet ->
      BatchDeliveryLocation(
        id = fieldSet.readString("intervention_id"),
        providerName = fieldSet.readString("provider_name"),
        pduRef = fieldSet.readString("pdu_ref_id"),
        status = fieldSet.readString("status"),
      )
    }
    .build()

  @Bean
  fun deliveryLocationWriter(): ItemWriter<BatchDeliveryLocation> = ItemWriter { items ->
    items.forEach { item ->
      if (item.status == "D") {
        logger.info { "Deleting DeliveryLocation with ID: ${item.id}" }
        deliveryLocationRepository.deleteByPduRefIdAndInterventionId(item.pduRef, UUID.fromString(item.id))
      } else {
        logger.info { "Saving DeliveryLocation with ID: ${item.id}" }
        mapToDeliveryLocationEntity(item)?.let {
          deliveryLocationRepository.save(it)
        }
      }
    }
  }

  @Bean
  fun deliveryLocationStep(): Step = StepBuilder("deliveryLocationStep", jobRepository)
    .chunk<BatchDeliveryLocation, BatchDeliveryLocation>(10, transactionManager)
    .reader(deliveryLocationReader())
    .writer(deliveryLocationWriter())
    .build()

  private fun mapToDeliveryLocationEntity(
    batchDeliveryLocation: BatchDeliveryLocation,
  ): DeliveryLocation? {
    val interventionCatalogueId = UUID.fromString(batchDeliveryLocation.id)

    val existingDeliveryLocation = deliveryLocationRepository.findByPduRefIdAndInterventionId(
      batchDeliveryLocation.pduRef,
      interventionCatalogueId,
    )

    if (existingDeliveryLocation != null) {
      logger.info("DeliveryLocation already exists for pduRef ID: ${batchDeliveryLocation.pduRef} and intervention ID: $interventionCatalogueId")

      var isUpdated = false
      if (existingDeliveryLocation.providerName != batchDeliveryLocation.providerName) {
        existingDeliveryLocation.providerName = batchDeliveryLocation.providerName
        isUpdated = true
      }

      if (isUpdated) {
        logger.info("Updating existing DeliveryLocation with ID: ${existingDeliveryLocation.id}")
        return deliveryLocationRepository.save(existingDeliveryLocation)
      }
      return null
    }
    val interventionCatalogue = interventionCatalogueRepository.findById(interventionCatalogueId)
      .orElseThrow { IllegalArgumentException("Intervention not found for ID: $interventionCatalogueId") }

    val pduRef = pduRefRepository.findById(batchDeliveryLocation.pduRef)
      .orElseThrow { IllegalArgumentException("PduRef not found for ID: ${batchDeliveryLocation.pduRef}") }

    logger.info { "Creating new DeliveryLocation with pduRef ID: ${batchDeliveryLocation.pduRef} and intervention ID: $interventionCatalogueId" }

    return DeliveryLocation(
      id = UUID.randomUUID(),
      providerName = batchDeliveryLocation.providerName,
      contact = "",
      pduRef = pduRef,
      intervention = interventionCatalogue,
    )
  }
}
