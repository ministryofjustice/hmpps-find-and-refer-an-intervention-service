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
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogueToCourseMap
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.CourseRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueToCourseMapRepository
import java.util.UUID

data class BatchInterventionCatalogueToCourseMap(
  val interventionCatalogueId: String,
  val courseId: String,
  val status: String,
)

@Configuration
@EnableTransactionManagement
class LoadInterventionCatalogueToCourseMapJobConfiguration(
  @Qualifier("jobRepository") private val jobRepository: JobRepository,
  private val interventionCatalogueRepository: InterventionCatalogueRepository,
  private val interventionCatalogueToCourseMapRepository: InterventionCatalogueToCourseMapRepository,
  private val courseRepository: CourseRepository,
  private val transactionManager: PlatformTransactionManager,
) {
  companion object : KLogging()

  @Bean
  fun loadInterventionCatalogueToCourseMapJob(): Job = JobBuilder("loadInterventionCatalogueToCourseMapJob", jobRepository)
    .incrementer(TimestampIncrementer())
    .start(interventionCatalogueToCourseMapStep())
    .build()

  @Bean
  fun interventionCatalogueToCourseMapReader(): ItemReader<BatchInterventionCatalogueToCourseMap> = FlatFileItemReaderBuilder<BatchInterventionCatalogueToCourseMap>()
    .name("interventionCatalogueToCourseMapReader")
    .resource(ClassPathResource("csv/intervention_catalogue_to_course_map.csv"))
    .linesToSkip(1)
    .delimited()
    .names("intervention_catalogue_id", "course_id", "status")
    .fieldSetMapper { fieldSet ->
      BatchInterventionCatalogueToCourseMap(
        interventionCatalogueId = fieldSet.readString("intervention_catalogue_id"),
        courseId = fieldSet.readString("course_id"),
        status = fieldSet.readString("status"),
      )
    }
    .build()

  @Bean
  fun interventionCatalogueToCourseMapWriter(): ItemWriter<BatchInterventionCatalogueToCourseMap> = ItemWriter { items ->
    items.forEach { item ->
      if (item.status == "D") {
        logger.info { "Deleting InterventionCatalogueToCourseMap with ID: ${item.courseId}" }
        interventionCatalogueToCourseMapRepository.deleteByInterventionCatalogueIdAndCourseId(
          UUID.fromString(item.interventionCatalogueId),
          UUID.fromString(item.courseId),
        )
      } else {
        logger.info { "Saving InterventionCatalogueToCourseMap with course ID: ${item.courseId}" }
        mapToInterventionCatalogueToCourseMapEntity(item)?.let {
          interventionCatalogueToCourseMapRepository.save(it)
        }
      }
    }
  }

  private fun mapToInterventionCatalogueToCourseMapEntity(
    batchMap: BatchInterventionCatalogueToCourseMap,
  ): InterventionCatalogueToCourseMap? {
    val existingMap = interventionCatalogueToCourseMapRepository.findByInterventionCatalogueIdAndCourseId(
      UUID.fromString(batchMap.interventionCatalogueId),
      UUID.fromString(batchMap.courseId),
    )

    if (existingMap != null) {
      logger.info("InterventionCatalogueToCourseMap already exists for intervention catalogue ID: ${batchMap.interventionCatalogueId} and course ID: ${batchMap.courseId}")
      return null
    }
    val interventionCatalogue = interventionCatalogueRepository.findById(UUID.fromString(batchMap.interventionCatalogueId))
      .orElseGet {
        logger.error("InterventionCatalogue not found for ID: ${batchMap.interventionCatalogueId}")
        null
      } ?: return null

    val course = courseRepository.findById(UUID.fromString(batchMap.courseId))
      .orElseGet {
        logger.error("Course not found for ID: ${batchMap.courseId}")
        null
      } ?: return null

    return InterventionCatalogueToCourseMap(
      interventionCatalogue = interventionCatalogue,
      course = course,
    )
  }

  @Bean
  fun interventionCatalogueToCourseMapStep(): Step = StepBuilder("interventionCatalogueToCourseMapStep", jobRepository)
    .chunk<BatchInterventionCatalogueToCourseMap, BatchInterventionCatalogueToCourseMap>(10, transactionManager)
    .reader(interventionCatalogueToCourseMapReader())
    .writer(interventionCatalogueToCourseMapWriter())
    .build()
}
