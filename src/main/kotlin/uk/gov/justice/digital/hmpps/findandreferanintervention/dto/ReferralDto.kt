package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonReferenceType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SourcedFromReferenceType
import java.util.UUID

data class ReferralDto(
  val id: UUID,
  val settingType: SettingType,
  val interventionType: InterventionType,
  val interventionName: String,
  val personReference: String,
  val personReferenceType: PersonReferenceType,
  val sourcedFromReferenceType: SourcedFromReferenceType,
  val sourcedFromReference: String,
)
