package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonReferenceType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Referral
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SourcedFromReferenceType
import java.util.UUID

data class ReferralDetailsDto(
  val interventionType: InterventionType,
  val interventionName: String,
  val personReference: String,
  val personReferenceType: PersonReferenceType,
  val referralId: UUID,
  val setting: SettingType,
  val sourcedFromReference: String,
  val sourcedFromReferenceType: SourcedFromReferenceType,
)

fun Referral.toDto(): ReferralDetailsDto = ReferralDetailsDto(
  interventionType = this.interventionType,
  interventionName = this.interventionName,
  personReference = this.personReference,
  personReferenceType = this.personReferenceType,
  referralId = this.id,
  setting = this.settingType,
  sourcedFromReference = this.sourcedFromReference,
  sourcedFromReferenceType = this.sourcedFromReferenceType,
)
