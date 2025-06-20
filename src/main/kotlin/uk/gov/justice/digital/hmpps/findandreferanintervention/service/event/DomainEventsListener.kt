package uk.gov.justice.digital.hmpps.findandreferanintervention.service.event

import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DomainEventsListener {
  private val logger = LoggerFactory.getLogger(this::class.java)

  @SqsListener("hmppsdomaineventsqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun receive(notification: Notification) {
    when (notification.eventType) {
      REQUIREMENT_CREATED -> logger.info("probation-case.requirement.created event \n ${notification.message}")
      LICENCE_CONDITION_CREATED -> logger.info("probation-case.licence-condition.created event \n ${notification.message}")
      else -> logger.error("Unknown event type ${notification.eventType}")
    }
  }

  companion object {
    const val REQUIREMENT_CREATED = "probation-case.requirement.created"
    const val LICENCE_CONDITION_CREATED = "probation-case.licence-condition.created"
  }
}
