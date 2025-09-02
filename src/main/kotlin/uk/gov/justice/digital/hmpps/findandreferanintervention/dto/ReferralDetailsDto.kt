package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonReferenceType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Referral
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SourcedFromReferenceType
import java.util.UUID

@Schema(description = "Full details of the Referral")
data class ReferralDetailsDto(
  @field:Schema(description = "The type of intervention", example = "ACP")
  val interventionType: InterventionType,

  @field:Schema(description = "The name of the intervention")
  val interventionName: String,

  @field:Schema(description = "The person reference (CRN or NOMS number, see personReferencType)")
  val personReference: String,

  @field:Schema(description = "The type of person reference, detailed in personReference")
  val personReferenceType: PersonReferenceType,

  @field:Schema(description = "The unique identifier for this referral")
  val referralId: UUID,

  @field:Schema(description = "The setting where the intervention will take place", example = "COMMUNITY")
  val setting: SettingType,

  @field:Schema(description = "The upstream event reference type", example = "requirement")
  val sourcedFromReference: String,

  @field:Schema(description = "A unique identifier to the sourceFromReference", example = "abc123")
  val sourcedFromReferenceType: SourcedFromReferenceType,

  @field:Schema(description = "The event number from the source system")
  val eventNumber: Int,
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
  eventNumber = this.eventNumber,
)
