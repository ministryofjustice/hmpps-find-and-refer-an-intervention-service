package uk.gov.justice.digital.hmpps.hmppsinterventionsservice.jobs.scheduled.loadinterventions

import com.fasterxml.jackson.annotation.JsonProperty
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType

data class InterventionCatalogDefinition(
  @JsonProperty("intervention catalogue") val catalogue: InterventionCatalogEntryDefinition,
  @JsonProperty("delivery location") val deliveryLocation: Array<DeliveryLocationDefinition>,
  @JsonProperty("delivery method") val deliveryMethod: Array<DeliveryMethodDefinition>,
  @JsonProperty("delivery method setting") val deliveryMethodSetting: Array<DeliveryMethodSettingDefinition>,
  @JsonProperty("enabling intervention") val enablingIntervention: EnablingInterventionDefinition,
  @JsonProperty("criminogenic need") val criminogenicNeed: CriminogenicNeedDefinition,
  @JsonProperty("personal eligibility") val personalEligibility: PersonalEligibilityDefinition,
  @JsonProperty("eligibility offences") val eligibleOffence: EligibilityOffencesDefinition,
  @JsonProperty("excluded offences") val excludedOffenceOffence: ExcludedOffencesDefinition,
  @JsonProperty("possible outcome") val possibleOutcome: Array<PossibleOutcomeDefinition>,
  @JsonProperty("risk consideration") val riskConsideration: RiskConsiderationDefinition,
  @JsonProperty("special educational need") val specialEducationalNeed: SpecialEducationalNeedDefinition,
  @JsonProperty("exclusion definition") val exclusion: ExclusionDefinition,
)

data class InterventionCatalogEntryDefinition(
  @JsonProperty("name") val name: String,
  @JsonProperty("int type") val interventionType: InterventionType,
  @JsonProperty("short description") val shortDescription: String,
  @JsonProperty("long description") val longDescription: String,
  @JsonProperty("topic") val topic: String,
  @JsonProperty("session detail") val sessionDetail: String,
  @JsonProperty("reason for referral") val reasonForReferral: String,
)

data class DeliveryLocationDefinition(
  @JsonProperty("provider name") val providerName: String,
  @JsonProperty("contact") val contact: String,
  @JsonProperty("pdu establishments") val pduEstabishments: String,
)

data class DeliveryMethodDefinition(
  @JsonProperty("delivery method description") val deliveryMethodDescription: String,
  @JsonProperty("delivery format") val deliveryFormat: String,
  @JsonProperty("attendance type") val attendanceType: String,
)

data class DeliveryMethodSettingDefinition(
  @JsonProperty("delivery method setting") val deliveryMethodSetting: SettingType,
)

data class EnablingInterventionDefinition(
  @JsonProperty("enabling intervention detail") val enablingInterventionDetail: String,
)

data class CriminogenicNeedDefinition(
  @JsonProperty("need_id") val needId: String,
)

data class PersonalEligibilityDefinition(
  @JsonProperty("min age") val minAge: String,
  @JsonProperty("max age") val maxAge: String,
  @JsonProperty("males") val males: Boolean,
  @JsonProperty("females") val females: Boolean,
)

data class EligibilityOffencesDefinition(
  @JsonProperty("offence_type_id") val offenceTypeId: String,
  @JsonProperty("victim_type") val victimType: String,
  @JsonProperty("motivation") val motivation: String,
)

data class ExcludedOffencesDefinition(
  @JsonProperty("offence_type_id") val offenceTypeId: String,
  @JsonProperty("victim_type") val victimType: String,
  @JsonProperty("motivation") val motivation: String,
)

data class PossibleOutcomeDefinition(
  @JsonProperty("outcome") val outcome: String,
)

data class RiskConsiderationDefinition(
  @JsonProperty("cn score guide") val cnScoreGuide: String,
  @JsonProperty("extremism risk guide") val extremismRiskGuide: String,
  @JsonProperty("sara partner score guide") val saraPartnerScoreGuide: String,
  @JsonProperty("sara other score guide") val saraOtherScoreGuide: String,
  @JsonProperty("osp score guide") val ospScoreGuide: String,
  @JsonProperty("osp dc icc combination guide") val ospDcIccScoreGuide: String,
  @JsonProperty("ogrs score guide") val ogrsScoreGuide: String,
  @JsonProperty("ovp guide") val ovpGuide: String,
  @JsonProperty("ogp guide") val ogpGuide: String,
  @JsonProperty("pna guide") val pnaGuide: String,
  @JsonProperty("rosh level") val roshLevel: String,
  @JsonProperty("rsr guide") val rsrGuide: String,
)

data class SpecialEducationalNeedDefinition(
  @JsonProperty("literacy level guide") val literacyLevelGuide: String,
  @JsonProperty("learning disability catered for") val learningDisabilityCateredFor: String,
  @JsonProperty("equivalent non ldc programme guide") val equivalentNonLdcProgrammeGuide: String,
)

data class ExclusionDefinition(
  @JsonProperty("min remaining sentence duration guide") val minRemaingSentenceGuide: String,
  @JsonProperty("remaining license community order guide") val remainingLicenseCommunityOrderGuide: String,
  @JsonProperty("alcohol drug problem guide") val alcoholDrugProblemGuide: String,
  @JsonProperty("mental health problem guide") val mentalHealthProblemGuide: String,
  @JsonProperty("other preferred method guide") val otherPreparedMethodGuide: String,
  @JsonProperty("same type rule guide") val sameTypeRuleGuide: String,
  @JsonProperty("schedule frequency guide") val scheduleRequencyGuide: String,
)
