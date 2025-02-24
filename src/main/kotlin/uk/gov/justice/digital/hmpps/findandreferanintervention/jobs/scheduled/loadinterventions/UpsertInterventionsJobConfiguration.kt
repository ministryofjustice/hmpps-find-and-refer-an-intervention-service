package uk.gov.justice.digital.hmpps.hmppsinterventionsservice.jobs.scheduled.loadinterventions

import mu.KLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.OnStartupJobLauncherFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.TimestampIncrementer
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

  /*@Bean
  fun jobLauncherCommandlineRunner(jobLauncher: TaskExecutorJobLauncher, upsertInterventionsJob: Job): CommandLineRunner {
    return CommandLineRunner { jobLauncher.run(upsertInterventionsJob, null) }
  }*/

  /*  @Bean
  fun upsertProvidersStep(
    providerSetup: OutcomeSetupTasklet,
    platformTransactionManager: PlatformTransactionManager,
  ): Step = StepBuilder("upsertProvidersStep", jobRepository)
    .tasklet(providerSetup, platformTransactionManager)
    .build()

  @Bean
  fun upsertContractDetailsStep(
    contractDetailsSetupTasklet: InterventionSetupTasklet,
    platformTransactionManager: PlatformTransactionManager,
  ): Step = StepBuilder("upsertContractDetailsStep", jobRepository)
    .tasklet(contractDetailsSetupTasklet, platformTransactionManager)
    .build()
*/
  @Bean
  fun upsertInterventionsJob(upsertInterventionsStep: Step, jobRepository: JobRepository): Job {
    logger("starting to instantiate Job...")

    val job: Job = JobBuilder("upsertInterventionsJob", jobRepository)
      .incrementer(TimestampIncrementer())
      .start(upsertInterventionsStep)
      .build()

    logger("created job... : " + job.name)

    return job
  }

  @Bean
  fun upsertInterventionsStep(
    reader: InterventionDefinitionReader,
    processor: UpsertInterventionProcessor,
    platformTransactionManager: PlatformTransactionManager,
  ): Step = StepBuilder("upsertInterventionsStep", jobRepository)
    .chunk<InterventionCatalogDefinition, InterventionCatalogue>(10, platformTransactionManager)
    .reader(reader)
    .processor(processor)
    .transactionManager(platformTransactionManager)
    .build()
}
