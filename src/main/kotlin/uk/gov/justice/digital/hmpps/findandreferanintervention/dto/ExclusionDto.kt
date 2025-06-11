package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Exclusion

// Excludes any properties that have null values when creating dto.
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ExclusionDto(
  val minRemainingSentenceDuration: String? = null,
  val remainingLicenseCommunityOrder: String? = null,
  val alcoholDrugProblem: String? = null,
  val mentalHealthProblem: String? = null,
  val otherPreferredMethod: String? = null,
  val sameTypeRule: String? = null,
  val scheduleFrequency: String? = null,
  val notAllowedIfEligibleForAnotherIntervention: String? = null,
  val literacyLevel: String? = null,
  val convictedForOffenceTypeGuide: String? = null,
)

fun Exclusion.toDto(): ExclusionDto = ExclusionDto(
  minRemainingSentenceDuration = this.minRemainingSentenceDurationGuide,
  remainingLicenseCommunityOrder = this.remainingLicenseCommunityOrderGuide,
  alcoholDrugProblem = this.alcoholDrugProblemGuide,
  mentalHealthProblem = this.mentalHealthProblemGuide,
  otherPreferredMethod = this.otherPreferredMethodGuide,
  sameTypeRule = this.sameTypeRuleGuide,
  scheduleFrequency = this.scheduleFrequencyGuide,
  notAllowedIfEligibleForAnotherIntervention = this.notAllowedIfEligibleForAnotherInterventionGuide,
  literacyLevel = this.literacyLevelGuide,
  convictedForOffenceTypeGuide = this.convictedForOffenceTypeGuide,
)
