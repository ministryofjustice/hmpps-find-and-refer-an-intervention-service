package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata

import mu.KLogging
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.Step
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.infrastructure.item.ItemReader
import org.springframework.batch.infrastructure.item.ItemWriter
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.OnStartupJobLauncherFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.TimestampIncrementer
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryLocationRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PduRefRepository
import java.util.UUID

data class BatchDeliveryLocation(
  val id: String,
  val interventionId: String,
  val providerName: String = "",
  val pduRef: String,
  val contact: String,
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
  private val onStartupJobLauncherFactory: OnStartupJobLauncherFactory,

) {
  companion object : KLogging()

  @Bean
  fun loadDeliveryLocationJobLauncher(loadDeliveryLocationJob: Job): ApplicationRunner = onStartupJobLauncherFactory.makeBatchLauncher(loadDeliveryLocationJob)

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
    .names("id", "intervention_id", "provider_name", "pdu_ref_id", "contact", "status")
    .fieldSetMapper { fieldSet ->
      BatchDeliveryLocation(
        id = fieldSet.readString("id")!!,
        interventionId = fieldSet.readString("intervention_id")!!,
        providerName = fieldSet.readString("provider_name")!!,
        pduRef = fieldSet.readString("pdu_ref_id")!!,
        contact = fieldSet.readString("contact")!!,
        status = fieldSet.readString("status")!!,
      )
    }
    .build()

  @Bean
  fun deliveryLocationWriter(): ItemWriter<BatchDeliveryLocation> = ItemWriter { items ->
    items.forEach { item ->
      if (item.status == "D") {
        logger.info { "Deleting DeliveryLocation with ID: ${item.interventionId}" }
        deliveryLocationRepository.deleteByPduRefIdAndInterventionIdAndProviderName(
          item.pduRef,
          UUID.fromString(item.interventionId),
          item.providerName,
        )
      } else {
        mapToDeliveryLocationEntity(item)?.let {
          deliveryLocationRepository.save(it)
        }
      }
    }
  }

  @Bean
  fun deliveryLocationStep(): Step = StepBuilder("deliveryLocationStep", jobRepository)
    .chunk<BatchDeliveryLocation, BatchDeliveryLocation>(10)
    .reader(deliveryLocationReader())
    .writer(deliveryLocationWriter())
    .transactionManager(transactionManager)
    .build()

  private fun mapToDeliveryLocationEntity(
    batchDeliveryLocation: BatchDeliveryLocation,
  ): DeliveryLocation? {
    val interventionCatalogueId = UUID.fromString(batchDeliveryLocation.interventionId)

    val existingDeliveryLocation = deliveryLocationRepository.findByPduRefIdAndInterventionIdAndProviderName(
      batchDeliveryLocation.pduRef,
      interventionCatalogueId,
      batchDeliveryLocation.providerName,
    )

    if (existingDeliveryLocation != null) {
      return updateExistingDeliveryLocation(batchDeliveryLocation, interventionCatalogueId, existingDeliveryLocation)
    }
    val interventionCatalogue = interventionCatalogueRepository.findById(interventionCatalogueId).orElseGet {
      logger.error { "InterventionCatalogue not found for ID: $interventionCatalogueId" }
      null
    } ?: return null

    val pduRef = pduRefRepository.findById(batchDeliveryLocation.pduRef).orElseGet {
      logger.error { "PduRef not found for ID: ${batchDeliveryLocation.pduRef}" }
      null
    } ?: return null

    logger.info { "Creating new DeliveryLocation with pduRef ID: ${batchDeliveryLocation.pduRef} and intervention ID: $interventionCatalogueId" }

    return DeliveryLocation(
      id = UUID.fromString(batchDeliveryLocation.id),
      providerName = batchDeliveryLocation.providerName,
      contact = "",
      pduRef = pduRef,
      intervention = interventionCatalogue,
    )
  }

  private fun updateExistingDeliveryLocation(
    batchDeliveryLocation: BatchDeliveryLocation,
    interventionCatalogueId: UUID?,
    existingDeliveryLocation: DeliveryLocation,
  ): DeliveryLocation? {
    logger.info(
      "DeliveryLocation already exists for pduRef ID: ${batchDeliveryLocation.pduRef} and intervention ID: $interventionCatalogueId" +
        " and providerName: ${batchDeliveryLocation.providerName}",
    )

    var isUpdated = false
    if (existingDeliveryLocation.providerName != batchDeliveryLocation.providerName) {
      existingDeliveryLocation.providerName = batchDeliveryLocation.providerName
      isUpdated = true
    }

    if (existingDeliveryLocation.contact != batchDeliveryLocation.contact) {
      existingDeliveryLocation.contact = batchDeliveryLocation.contact
      isUpdated = true
    }

    if (isUpdated) {
      logger.info("Updating existing DeliveryLocation with ID: ${existingDeliveryLocation.id}")
      return deliveryLocationRepository.save(existingDeliveryLocation)
    }
    return null
  }
}
