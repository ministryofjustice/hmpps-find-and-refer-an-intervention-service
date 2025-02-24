package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled

import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service

@Service
class ApplicationStartup(
  val jobLauncherFactory: OnStartupJobLauncherFactory,
  private val asyncJobLauncher: JobLauncher,
  private val batchUtils: BatchUtils,
  // private val upsertInterventionsJob: Job,
) : ApplicationListener<ApplicationReadyEvent> {
  /**
   * This event is executed as late as conceivably possible to indicate that
   * the application is ready to service requests.
   */
  override fun onApplicationEvent(event: ApplicationReadyEvent) {
    // jobLauncherFactory.makeBatchLauncher(upsertInterventionsJob).run(null)
    // asyncJobLauncher.run(upsertInterventionsJob, JobParameters())
  }

  /*@Bean
  fun upsertInterventionsJob(upsertInterventionCatalogueStep: Step): Job = JobBuilder("upsertInterventionsJob", jobRepository)
    .incrementer(TimestampIncrementer())
    .start(upsertInterventionCatalogueStep)
    .build()*/
}
