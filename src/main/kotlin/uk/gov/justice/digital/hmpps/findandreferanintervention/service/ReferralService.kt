package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.HmppsDomainEvent
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Referral
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SourcedFromReferenceType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.MessageRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.ReferralRepository
import java.util.UUID

@Service
@Transactional
class ReferralService(
  private val referralRepository: ReferralRepository,
  private val messageRepository: MessageRepository,
) {

  private val logger = LoggerFactory.getLogger(this::class.java)

  fun handleRequirementCreatedEvent(hmppsDomainEvent: HmppsDomainEvent, messageId: UUID) {
    logger.info("Saving requirement-condition.created created event to db with requirementId: ${hmppsDomainEvent.additionalInformation["requirementId"]}")
    val newReferral =
      referralRepository.save(
        Referral(
          id = UUID.randomUUID(),
          settingType = SettingType.COMMUNITY,
          interventionType = InterventionType.ACP,
          interventionName = hmppsDomainEvent.additionalInformation.getValue("requirementSubType") as String,
          personReferenceType = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().first,
          personReference = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().second!!,
          sourcedFromReferenceType = SourcedFromReferenceType.REQUIREMENT,
          sourcedFromReference = hmppsDomainEvent.additionalInformation["requirementId"] as String,
        ),
      )
    val message = messageRepository.getReferenceById(messageId)
    message.referral = newReferral
    messageRepository.save(message)
  }

  fun handleLicenceConditionCreatedEvent(hmppsDomainEvent: HmppsDomainEvent, messageId: UUID) {
    logger.info("Saving licence-condition.created event to db with licconiditionId: ${hmppsDomainEvent.additionalInformation["licconiditionId"]}")
    val referral = referralRepository.save(
      Referral(
        id = UUID.randomUUID(),
        settingType = SettingType.COMMUNITY,
        interventionType = InterventionType.ACP,
        interventionName = hmppsDomainEvent.additionalInformation.getValue("licconditionSubType") as String,
        personReferenceType = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().first,
        personReference = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().second!!,
        sourcedFromReferenceType = SourcedFromReferenceType.LICENCE_CONDITION,
        sourcedFromReference = hmppsDomainEvent.additionalInformation["licconiditionId"] as String,
      ),
    )
    val message = messageRepository.getReferenceById(messageId)
    message.referral = referral
    messageRepository.save(message)
  }
}
