package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class InterventionCatalogueDefinition(
  @JsonProperty("uuid") val uuid: UUID,
  @JsonProperty("intervention_catalogue") val catalogue: InterventionCatalogueEntryDefinition,
  @JsonProperty("delivery_location") val deliveryLocation: Array<DeliveryLocationDefinition>?,
  @JsonProperty("delivery_methods") val deliveryMethod: Array<DeliveryMethodDefinition>?,
  @JsonProperty("delivery_method_settings") val deliveryMethodSetting: Array<String>?,
  @JsonProperty("enabling_intervention_detail") val enablingIntervention: String?,
  @JsonProperty("criminogenic_needs") val criminogenicNeed: Array<String>,
  @JsonProperty("personal_eligibility") val personalEligibility: PersonalEligibilityDefinition?,
  @JsonProperty("eligibility_offence") val eligibleOffence: Array<EligibleOffenceDefinition>?,
  @JsonProperty("exclude_offence") val excludedOffence: Array<ExcludedOffencesDefinition>?,
  @JsonProperty("possible_outcome") val possibleOutcome: Array<String>?,
  @JsonProperty("risk_consideration") val riskConsideration: RiskConsiderationDefinition?,
  @JsonProperty("special_educational_needs") val specialEducationalNeed: SpecialEducationalNeedDefinition?,
  @JsonProperty("exclusion") val exclusion: ExclusionDefinition?,
)

data class InterventionCatalogueEntryDefinition(
  @JsonProperty("name") val name: String,
  @JsonProperty("int_type") val interventionType: String,
  @JsonProperty("short_description") val shortDescription: String,
  @JsonProperty("long_description") val longDescription: String?,
  @JsonProperty("topic") val topic: String?,
  @JsonProperty("session_detail") val sessionDetail: String?,
  @JsonProperty("reason_for_referral") val reasonForReferral: String,
  @JsonProperty("time_to_complete") val timeToComplete: String?,
)

data class DeliveryLocationDefinition(
  @JsonProperty("provider_name") val providerName: String,
  @JsonProperty("contact") val contact: String,
  @JsonProperty("pdu_establishments") val pduRefs: Array<String>,
)

data class DeliveryMethodDefinition(
  @JsonProperty("delivery_method_description") val deliveryMethodDescription: String?,
  @JsonProperty("format") val deliveryFormat: String?,
  @JsonProperty("attendance_type") val attendanceType: String?,
)

data class PersonalEligibilityDefinition(
  @JsonProperty("min_age") val minAge: String?,
  @JsonProperty("max_age") val maxAge: String?,
  @JsonProperty("males") val males: Boolean,
  @JsonProperty("females") val females: Boolean,
)

data class EligibleOffenceDefinition(
  @JsonProperty("offence_type_id") val offenceTypeId: String,
  @JsonProperty("victim_type") val victimType: String?,
  @JsonProperty("motivation") val motivation: String?,
)

data class ExcludedOffencesDefinition(
  @JsonProperty("offence_type_id") val offenceTypeId: String,
  @JsonProperty("victim_type") val victimType: String?,
  @JsonProperty("motivation") val motivation: String?,
)

data class RiskConsiderationDefinition(
  @JsonProperty("cn_score_guide") val cnScoreGuide: String?,
  @JsonProperty("extremism_risk_guide") val extremismRiskGuide: String?,
  @JsonProperty("sara_partner_score_guide") val saraPartnerScoreGuide: String?,
  @JsonProperty("sara_other_score_guide") val saraOtherScoreGuide: String?,
  @JsonProperty("osp_score_guide") val ospScoreGuide: String?,
  @JsonProperty("osp_dc_icc_combination_guide") val ospDcIccCombinationGuide: String?,
  @JsonProperty("ogrs_score_guide") val ogrsScoreGuide: String?,
  @JsonProperty("ovp_guide") val ovpGuide: String?,
  @JsonProperty("ogp_guide") val ogpGuide: String?,
  @JsonProperty("pna_guide") val pnaGuide: String?,
  @JsonProperty("rosh_level") val roshLevel: String?,
  @JsonProperty("rsr_guide") val rsrGuide: String?,
)

data class SpecialEducationalNeedDefinition(
  @JsonProperty("exclusion_literacy_level_guide") val literacyLevelGuide: String?,
  @JsonProperty("learning_disability_catered_for") val learningDisabilityCateredFor: String?,
  @JsonProperty("equivalent_non_ldc_programme_guide") val equivalentNonLdcProgrammeGuide: String?,
)

data class ExclusionDefinition(
  @JsonProperty("min_remaining_sentence_duration_guide") val minRemaingSentenceGuide: String?,
  @JsonProperty("remaining_license_community_order_guide") val remainingLicenseCommunityOrderGuide: String?,
  @JsonProperty("alcohol_drug_problem_guide") val alcoholDrugProblemGuide: String?,
  @JsonProperty("mental_health_problem_guide") val mentalHealthProblemGuide: String?,
  @JsonProperty("other_preferred_method_guide") val otherPreferredMethodGuide: String?,
  @JsonProperty("same_type_rule_guide") val sameTypeRuleGuide: String?,
  @JsonProperty("schedule_frequency_guide") val scheduleRequencyGuide: String?,
)
