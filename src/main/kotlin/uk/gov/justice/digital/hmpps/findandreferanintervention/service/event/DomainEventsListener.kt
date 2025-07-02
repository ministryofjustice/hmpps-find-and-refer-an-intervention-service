package uk.gov.justice.digital.hmpps.findandreferanintervention.service.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.HmppsDomainEvent
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Message
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.MessageRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ReferralService
import java.util.UUID

@Service
class DomainEventsListener(
  private val objectMapper: ObjectMapper,
  private val referralService: ReferralService,
  private val messageRepository: MessageRepository,

) {
  private val logger = LoggerFactory.getLogger(this::class.java)

  @SqsListener("hmppsdomaineventsqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun receive(sqsMessage: SqsMessage) {
    when (sqsMessage.eventType) {
      REQUIREMENT_CREATED, LICENCE_CONDITION_CREATED -> handleHmppsDomainEvent(sqsMessage)
      else -> logger.info("Unknown event type ${sqsMessage.eventType}")
    }
  }

  private fun handleHmppsDomainEvent(sqsMessage: SqsMessage) {
    val message = messageRepository.findByIdOrNull(sqsMessage.messageId)
    if (message == null) {
      logger.info("Inserting Event with Id: ${sqsMessage.messageId}")
      val messageId: UUID = messageRepository.save(
        Message(
          id = sqsMessage.messageId,
          referral = null,
          event = sqsMessage,
        ),
      ).id
      val hmppsDomainEvent: HmppsDomainEvent = objectMapper.readValue(sqsMessage.message)
      when (sqsMessage.eventType) {
        REQUIREMENT_CREATED -> referralService.handleRequirementCreatedEvent(hmppsDomainEvent, messageId)
        LICENCE_CONDITION_CREATED -> referralService.handleLicenceConditionCreatedEvent(hmppsDomainEvent, messageId)
      }
    } else {
      return logger.info("Event with Id: ${sqsMessage.messageId} already exists. Skipping insert.")
    }
  }

  companion object {
    const val REQUIREMENT_CREATED = "probation-case.requirement.created"
    const val LICENCE_CONDITION_CREATED = "probation-case.licence-condition.created"
  }
}
