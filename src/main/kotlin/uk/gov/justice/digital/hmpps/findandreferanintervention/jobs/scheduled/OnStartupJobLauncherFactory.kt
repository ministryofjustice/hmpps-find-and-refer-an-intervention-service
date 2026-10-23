package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled

import mu.KLogging
import net.logstash.logback.argument.StructuredArguments
import org.springframework.batch.core.converter.DefaultJobParametersConverter
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.JobExecution
import org.springframework.batch.core.job.parameters.JobParameters
import org.springframework.batch.core.launch.JobOperator
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import kotlin.system.exitProcess

@Component
class OnStartupJobLauncherFactory(
  private val jobOperator: JobOperator,
) {
  companion object : KLogging()

  private val exitCodeMapper = SimpleJvmExitCodeMapper()
  private val jobParametersConverter = DefaultJobParametersConverter()
  private val timestampIncrementer = TimestampIncrementer()

  fun makeBatchLauncher(job: Job): ApplicationRunner = makeLauncher(job.name) { args ->
    val jobParameters = jobParametersConverter.getJobParameters(
      StringUtils.splitArrayElementsIntoProperties(args.nonOptionArgs.toTypedArray(), "=") ?: java.util.Properties(),
    )
    val execution = startJob(job, jobParameters)
    exitCodeMapper.intValue(execution.exitStatus.exitCode)
  }

  fun startJob(job: Job, jobParameters: JobParameters): JobExecution {
    val nextParameters = timestampIncrementer.getNext(jobParameters)
    return jobOperator.start(job, nextParameters)
  }

  fun makeLauncher(jobName: String, entryPoint: (args: ApplicationArguments) -> Int): ApplicationRunner = ApplicationRunner { args ->
    if (args.getOptionValues("jobName")?.contains(jobName) == true) {
      logger.info("running one off job {}", StructuredArguments.kv("jobName", jobName))
      exitProcess(entryPoint(args))
    }
  }
}
