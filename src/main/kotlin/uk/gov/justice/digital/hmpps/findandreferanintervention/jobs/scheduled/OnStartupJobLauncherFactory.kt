package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled

import mu.KLogging
import net.logstash.logback.argument.StructuredArguments
import org.springframework.batch.core.converter.DefaultJobParametersConverter
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.launch.JobOperator
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import kotlin.system.exitProcess

@Component
class OnStartupJobLauncherFactory(private val jobOperator: JobOperator) {
  companion object : KLogging()

  private val exitCodeMapper = SimpleJvmExitCodeMapper()
  private val jobParametersConverter = DefaultJobParametersConverter()

  fun makeBatchLauncher(job: Job): ApplicationRunner = makeLauncher(job.name, buildEntryPoint(job, jobOperator))
  private fun buildEntryPoint(job: Job, jobOperator: JobOperator): (args: ApplicationArguments) -> Int {
    val entryPoint = fun(args: ApplicationArguments): Int {
      val rawParams = jobParametersConverter.getJobParameters(
        StringUtils.splitArrayElementsIntoProperties(args.nonOptionArgs.toTypedArray(), "=")!!,
      )

      val nextParams = job.jobParametersIncrementer?.getNext(rawParams) ?: rawParams

      val execution = jobOperator.start(job, nextParams)
      return exitCodeMapper.intValue(execution.exitStatus.exitCode)
    }

    return entryPoint
  }

  fun makeLauncher(jobName: String, entryPoint: (args: ApplicationArguments) -> Int): ApplicationRunner = ApplicationRunner { args ->
    if (args.getOptionValues("jobName")?.contains(jobName) == true) {
      logger.info("running one off job {}", StructuredArguments.kv("jobName", jobName))
      exitProcess(entryPoint(args))
    }
  }
}
