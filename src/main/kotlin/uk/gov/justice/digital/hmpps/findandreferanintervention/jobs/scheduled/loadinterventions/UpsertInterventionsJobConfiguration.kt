package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions

import mu.KLogging
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.job.parameters.JobParametersBuilder
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.Step
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.infrastructure.item.ItemReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.OnStartupJobLauncherFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue

@Configuration
@EnableTransactionManagement
class UpsertInterventionsJobConfiguration(
  private val onStartupJobLauncherFactory: OnStartupJobLauncherFactory,
  @Qualifier("jobRepository") private val jobRepository: JobRepository,
) {
  companion object : KLogging()

  @Bean
  fun upsertInterventionsJobLauncher(upsertInterventionsJob: Job): ApplicationRunner = onStartupJobLauncherFactory.makeBatchLauncher(upsertInterventionsJob)

  @Bean
  @Profile("local")
  fun jobLauncherCommandlineRunner(
    @Qualifier("upsertInterventionsJob") upsertInterventionsJob: Job,
    @Qualifier("loadDeliveryLocationJob") loadDeliveryLocationJob: Job,
    @Qualifier("loadInterventionCatalogueMapJob") loadInterventionCatalogueMapJob: Job,
    @Qualifier("loadInterventionCatalogueToCourseMapJob") loadInterventionCatalogueToCourseMapJob: Job,
  ): CommandLineRunner = CommandLineRunner { args ->
    try {
      listOf(upsertInterventionsJob, loadDeliveryLocationJob, loadInterventionCatalogueMapJob, loadInterventionCatalogueToCourseMapJob).forEach { job ->
        logger.info("Running job: ${job.name}")
        onStartupJobLauncherFactory.startJob(
          job,
          JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters(),
        )
      }
    } catch (e: JobExecutionAlreadyRunningException) {
      logger.error("Job already running, skipping execution", e)
    } catch (e: Exception) {
      logger.error("Error running job: ${e.message}", e)
    } finally {
      logger.info("Job execution completed")
    }
  }

  @Bean
  fun upsertInterventionsJob(upsertInterventionsStep: Step, jobRepository: JobRepository): Job {
    logger.info("starting to instantiate Job...")

    val job: Job = JobBuilder("upsertInterventionsJob", jobRepository)
      .start(upsertInterventionsStep)
      .build()

    logger.info("created job... : " + job.name)

    return job
  }

  @Bean
  fun readInterventionDefinition(): ItemReader<InterventionCatalogueDefinition> = InterventionDefinitionReader("classpath:/db/interventions/*.json")

  @Bean
  fun upsertInterventionsStep(
    reader: InterventionDefinitionReader,
    processor: UpsertInterventionProcessor,
    platformTransactionManager: PlatformTransactionManager,
  ): Step = StepBuilder("upsertInterventionsStep", jobRepository)
    .allowStartIfComplete(true)
    .chunk<InterventionCatalogueDefinition, InterventionCatalogue>(10)
    .reader(reader)
    .processor(processor)
    .writer {}
    .transactionManager(platformTransactionManager)
    .build()
}
