package uk.gov.justice.digital.hmpps.findandreferanintervention.service.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.MessageHistory
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.MessageHistoryRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ReferralService
import java.util.UUID

@Service
class DomainEventsListener(
  private val objectMapper: ObjectMapper,
  private val referralService: ReferralService,
  private val messageHistoryRepository: MessageHistoryRepository,
) {
  private val logger = LoggerFactory.getLogger(this::class.java)

  @SqsListener("hmppsdomaineventsqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun receive(notification: Notification) {
    messageHistoryRepository.save(MessageHistory(UUID.randomUUID(), notification))
    when (notification.eventType) {
      REQUIREMENT_CREATED -> referralService.handleRequirementCreatedEvent(objectMapper.readValue(notification.message))
      LICENCE_CONDITION_CREATED -> referralService.handleLicenceConditionCreatedEvent(
        objectMapper.readValue(
          notification.message,
        ),
      )

      else -> logger.error("Unknown event type ${notification.eventType}")
    }
  }

  companion object {
    const val REQUIREMENT_CREATED = "probation-case.requirement.created"
    const val LICENCE_CONDITION_CREATED = "probation-case.licence-condition.created"
  }
}
