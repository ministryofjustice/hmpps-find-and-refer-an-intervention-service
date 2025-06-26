package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Referral
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SourcedFromReferenceType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.ReferralRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.model.event.HmppsDomainEvent
import java.util.UUID

@Service
@Transactional
class ReferralService(private val referralRepository: ReferralRepository) {
  private val logger = LoggerFactory.getLogger(this::class.java)
  fun handleRequirementCreatedEvent(event: HmppsDomainEvent) {
    logger.info("Saving requirement-condition.created created event to db with requirementId: ${event.additionalInformation["requirementId"]}")
    referralRepository.save(
      Referral(
        id = UUID.randomUUID(),
        settingType = SettingType.COMMUNITY,
        interventionType = InterventionType.ACP,
        interventionName = event.additionalInformation.getValue("requirementSubType") as String,
        personReferenceType = event.personReference.getPersonReferenceTypeAndValue().first,
        personReference = event.personReference.getPersonReferenceTypeAndValue().second!!,
        sourcedFromReferenceType = SourcedFromReferenceType.REQUIREMENT,
        sourcedFromReference = event.additionalInformation["requirementId"] as String,
      ),
    )
  }

  fun handleLicenceConditionCreatedEvent(event: HmppsDomainEvent) {
    logger.info("Saving licence-condition.created event to db with licconiditionId: ${event.additionalInformation["licconiditionId"]}")
    referralRepository.save(
      Referral(
        id = UUID.randomUUID(),
        settingType = SettingType.COMMUNITY,
        interventionType = InterventionType.ACP,
        interventionName = event.additionalInformation.getValue("licconditionSubType") as String,
        personReferenceType = event.personReference.getPersonReferenceTypeAndValue().first,
        personReference = event.personReference.getPersonReferenceTypeAndValue().second!!,
        sourcedFromReferenceType = SourcedFromReferenceType.REQUIREMENT,
        sourcedFromReference = event.additionalInformation["licconiditionId"] as String,
      ),
    )
  }
}
