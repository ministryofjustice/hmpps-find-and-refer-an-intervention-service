package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions

import mu.KLogging
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class InterventionSetupTasklet @Autowired constructor() : Tasklet {
  companion object : KLogging()

  override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
    TODO("Not yet implemented")
  }
}
