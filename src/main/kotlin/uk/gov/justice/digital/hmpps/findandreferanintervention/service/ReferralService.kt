package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import com.microsoft.applicationinsights.TelemetryClient
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.logToAppInsights
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.ReferralDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.HmppsDomainEvent
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.getPersonReferenceTypeAndValue
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.listener.CommunityReferralCreatedEvent
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
  private val applicationEventPublisher: ApplicationEventPublisher,
  private val telemetryClient: TelemetryClient,
) {

  private val logger = LoggerFactory.getLogger(this::class.java)

  fun getReferralDetailsById(referralId: UUID): ReferralDetailsDto? = referralRepository.findReferralById(referralId)?.toDto()

  fun handleRequirementCreatedEvent(hmppsDomainEvent: HmppsDomainEvent, messageId: UUID) {
    telemetryClient.logToAppInsights(
      "Probation.case-requirement.created event received",
      mapOf(
        "eventType" to hmppsDomainEvent.eventType,
        "requirementID" to hmppsDomainEvent.additionalInformation["requirementID"].toString(),
        "crn" to hmppsDomainEvent.personReference.findCrn()!!,
      ),
    )
    val requirementMainType = hmppsDomainEvent.additionalInformation.getValue("requirementMainType")
    if (hmppsDomainEvent.additionalInformation.getValue("requirementMainType") == null || requirementMainType != "Court - Accredited Programme") {
      return logger.info("requirementMainType is not for creation of an Accredited Programme. requirementMainType: $requirementMainType and messageId: $messageId)")
    }
    val personReference: String = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().second!!
    val interventionName: String = hmppsDomainEvent.additionalInformation.getValue("requirementSubType") as String
    if (interventionName != "Building Choices") {
      return logger.info("requirement is not for Building Choices. interventionName: $interventionName")
    }
    val sourcedFromReference: String = hmppsDomainEvent.additionalInformation["requirementID"] as String
    val existingReferral = referralRepository.findByPersonReferenceAndInterventionNameAndSourcedFromReference(
      personReference,
      interventionName,
      sourcedFromReference,
    )
    if (existingReferral != null) {
      return logger.info("Duplicate request to create referral from requirement.created for Person reference: $personReference, Intervention Name: $interventionName and Reference Id: $sourcedFromReference")
    }

    logger.info("Saving requirement-condition.created created event to db with requirementId: ${hmppsDomainEvent.additionalInformation["requirementID"]}")

    val newReferral =
      referralRepository.save(
        Referral(
          id = UUID.randomUUID(),
          settingType = SettingType.COMMUNITY,
          interventionType = InterventionType.ACP,
          interventionName = hmppsDomainEvent.additionalInformation.getValue("requirementSubType").toString(),
          personReferenceType = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().first,
          personReference = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().second!!,
          sourcedFromReferenceType = SourcedFromReferenceType.REQUIREMENT,
          sourcedFromReference = sourcedFromReference,
          eventNumber = hmppsDomainEvent.additionalInformation.getValue("eventNumber").toString().toInt(),
        ),
      )
    val message = messageRepository.getReferenceById(messageId)
    message.referral = newReferral
    messageRepository.save(message)

    applicationEventPublisher.publishEvent(CommunityReferralCreatedEvent(newReferral.id))
  }

  fun handleLicenceConditionCreatedEvent(hmppsDomainEvent: HmppsDomainEvent, messageId: UUID) {
    telemetryClient.logToAppInsights(
      "Probation.case-requirement.created event received",
      mapOf(
        "eventType" to hmppsDomainEvent.eventType,
        "licconditionId" to hmppsDomainEvent.additionalInformation["licconditionId"].toString(),
        "crn" to hmppsDomainEvent.personReference.findCrn()!!,
      ),
    )
    val licconditionMainType = hmppsDomainEvent.additionalInformation.getValue("licconditionMainType")
    // We have seen both spellings of Licence in the dev environment.
    if (hmppsDomainEvent.additionalInformation.getValue("licconditionMainType") == null || (licconditionMainType != "License - Accredited Programme" && licconditionMainType != "Licence - Accredited Programme")) {
      return logger.info("licconditionMainType is not for creation of an Accredited Programme. licconditionMainType: $licconditionMainType and messageId: $messageId)")
    }
    val personReference: String = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().second!!
    val interventionName: String = hmppsDomainEvent.additionalInformation.getValue("licconditionSubType").toString()
    if (interventionName != "Building Choices") {
      return logger.info("licence condition is not for Building Choices. interventionName: $interventionName")
    }
    val sourcedFromReference: String = hmppsDomainEvent.additionalInformation["licconditionId"].toString()
    val existingReferral = referralRepository.findByPersonReferenceAndInterventionNameAndSourcedFromReference(
      personReference,
      interventionName,
      sourcedFromReference,
    )
    if (existingReferral != null) {
      return logger.info("Duplicate request to create referral from licence-condition.created event for Person reference: $personReference, Intervention Name: $interventionName and Reference Id: $sourcedFromReference")
    }
    logger.info("Saving licence-condition.created event to db with licconditionId: ${hmppsDomainEvent.additionalInformation["licconditionId"]}")
    val newReferral = referralRepository.save(
      Referral(
        id = UUID.randomUUID(),
        settingType = SettingType.COMMUNITY,
        interventionType = InterventionType.ACP,
        interventionName = hmppsDomainEvent.additionalInformation.getValue("licconditionSubType").toString(),
        personReferenceType = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().first,
        personReference = hmppsDomainEvent.personReference.getPersonReferenceTypeAndValue().second!!,
        sourcedFromReferenceType = SourcedFromReferenceType.LICENCE_CONDITION,
        sourcedFromReference = hmppsDomainEvent.additionalInformation["licconditionId"].toString(),
        eventNumber = hmppsDomainEvent.additionalInformation.getValue("eventNumber").toString().toInt(),
      ),
    )
    val message = messageRepository.getReferenceById(messageId)
    message.referral = newReferral
    messageRepository.save(message)
    applicationEventPublisher.publishEvent(CommunityReferralCreatedEvent(newReferral.id))
  }
}
