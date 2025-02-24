package uk.gov.justice.digital.hmpps.hmppsinterventionsservice.jobs.scheduled.loadinterventions

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
@Component
class OutcomeSetupTasklet @Autowired constructor(
  // val providerRepository: ServiceProviderRepository,
) : Tasklet {
  /*companion object : KLogging()

  override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
    logger.info("Setting up providers")

    val providers = InterventionLoadFileReaderHelper.getResource("classpath:providers/providers.json")
    ObjectMapper().readTree(providers)
      .map { upsertProvider(it.get("code").asText(), it.get("name").asText()) }
      .also { providerRepository.saveAll(it) }

    return RepeatStatus.FINISHED
  }

  private fun upsertProvider(code: String, name: String): ServiceProvider = providerRepository.findById(code).getOrElse {
    logger.info("Creating missing provider $code")
    ServiceProvider(code, name)
  }*/
  override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
    TODO("Not yet implemented")
  }
}
