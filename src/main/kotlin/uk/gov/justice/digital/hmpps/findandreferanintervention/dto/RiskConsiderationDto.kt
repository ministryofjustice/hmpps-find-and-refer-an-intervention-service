package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RiskConsideration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RoshLevel

@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes any properties that have null values when creating dto.
data class RiskConsiderationDto(
  val cnScoreGuide: String? = null,
  val extremismRiskGuide: String? = null,
  val saraPartnerScoreGuide: String? = null,
  val saraOtherScoreGuide: String? = null,
  val ospScoreGuide: String? = null,
  val ospDcIccCombinationGuide: String? = null,
  val ogrsScoreGuide: String? = null,
  val ovpGuide: String? = null,
  val ogpGuide: String? = null,
  val pnaGuide: String? = null,
  val rsrGuide: String? = null,
  val roshLevel: RoshLevel? = null,
)

fun RiskConsideration.toDto(): RiskConsiderationDto = RiskConsiderationDto(
  cnScoreGuide = this.cnScoreGuide,
  extremismRiskGuide = this.extremismRiskGuide,
  saraPartnerScoreGuide = this.saraPartnerScoreGuide,
  saraOtherScoreGuide = this.saraOtherScoreGuide,
  ospScoreGuide = this.ospScoreGuide,
  ospDcIccCombinationGuide = this.ospDcIccCombinationGuide,
  ogrsScoreGuide = this.ogrsScoreGuide,
  ovpGuide = this.ovpGuide,
  ogpGuide = this.ogpGuide,
  pnaGuide = this.pnaGuide,
  rsrGuide = this.rsrGuide,
  roshLevel = this.roshLevel,
)
