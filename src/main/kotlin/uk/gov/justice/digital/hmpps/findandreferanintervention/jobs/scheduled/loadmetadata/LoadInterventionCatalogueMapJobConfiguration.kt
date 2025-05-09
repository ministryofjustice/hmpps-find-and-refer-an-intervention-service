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
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogueMap
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueMapRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionRepository
import java.util.UUID

data class BatchInterventionCatalogueMap(
  val interventionId: String,
  val interventionCatalogueId: String,
  val status: String,
)

@Configuration
@EnableTransactionManagement
class LoadInterventionCatalogueMapJobConfiguration(
  @Qualifier("jobRepository") private val jobRepository: JobRepository,
  private val interventionCatalogueRepository: InterventionCatalogueRepository,
  private val interventionCatalogueMapRepository: InterventionCatalogueMapRepository,
  private val interventionRepository: InterventionRepository,
  private val transactionManager: PlatformTransactionManager,
) {
  companion object : KLogging()

  @Bean
  fun loadInterventionCatalogueMapJob(): Job = JobBuilder("loadInterventionCatalogueMapJob", jobRepository)
    .incrementer(TimestampIncrementer())
    .start(interventionCatalogueMapStep())
    .build()

  @Bean
  fun interventionCatalogueMapReader(): ItemReader<BatchInterventionCatalogueMap> = FlatFileItemReaderBuilder<BatchInterventionCatalogueMap>()
    .name("interventionCatalogueMapReader")
    .resource(ClassPathResource("csv/intervention_catalogue_map.csv"))
    .linesToSkip(1)
    .delimited()
    .names("intervention_id", "intervention_catalogue_id", "status")
    .fieldSetMapper { fieldSet ->
      BatchInterventionCatalogueMap(
        interventionId = fieldSet.readString("intervention_id"),
        interventionCatalogueId = fieldSet.readString("intervention_catalogue_id"),
        status = fieldSet.readString("status"),
      )
    }
    .build()

  @Bean
  fun interventionCatalogueMapWriter(): ItemWriter<BatchInterventionCatalogueMap> = ItemWriter { items ->
    items.forEach { item ->
      if (item.status == "D") {
        logger.info { "Deleting InterventionCatalogueMap with ID: ${item.interventionId}" }
        interventionCatalogueMapRepository.deleteByInterventionCatalogueIdAndInterventionId(
          UUID.fromString(item.interventionCatalogueId),
          UUID.fromString(item.interventionId),
        )
      } else {
        logger.info { "Saving InterventionCatalogueMap with intervention ID: ${item.interventionId}" }
        mapToInterventionCatalogueMapEntity(item)?.let {
          interventionCatalogueMapRepository.save(it)
        }
      }
    }
  }

  @Bean
  fun interventionCatalogueMapStep(): Step = StepBuilder("interventionCatalogueMapStep", jobRepository)
    .chunk<BatchInterventionCatalogueMap, BatchInterventionCatalogueMap>(10, transactionManager)
    .reader(interventionCatalogueMapReader())
    .writer(interventionCatalogueMapWriter())
    .build()

  private fun mapToInterventionCatalogueMapEntity(
    batchMap: BatchInterventionCatalogueMap,
  ): InterventionCatalogueMap? {
    val existingMap = interventionCatalogueMapRepository.findByInterventionIdAndInterventionCatalogueId(
      UUID.fromString(batchMap.interventionId),
      UUID.fromString(batchMap.interventionCatalogueId),
    )
    if (existingMap != null) {
      logger.info("InterventionCatalogueMap already exists for intervention ID: ${batchMap.interventionId} and intervention catalogue ID: ${batchMap.interventionCatalogueId}")
      return null
    }
    val interventionCatalogue = interventionCatalogueRepository.findById(UUID.fromString(batchMap.interventionCatalogueId))
      .orElseThrow { IllegalArgumentException("InterventionCatalogue not found for ID: ${batchMap.interventionCatalogueId}") }

    val intervention = interventionRepository.findById(UUID.fromString(batchMap.interventionId))
      .orElseThrow { IllegalArgumentException("Intervention not found for ID: ${batchMap.interventionId}") }

    return InterventionCatalogueMap(
      interventionCatalogue = interventionCatalogue,
      intervention = intervention,
    )
  }
}
