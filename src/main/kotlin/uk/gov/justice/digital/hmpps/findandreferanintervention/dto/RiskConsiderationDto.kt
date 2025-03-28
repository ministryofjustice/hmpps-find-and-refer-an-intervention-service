package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RiskConsideration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RoshLevel
import java.util.UUID

data class RiskConsiderationDto(
  val id: UUID,
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
) {

  val listOfRisks: () -> List<String> =
    {
      listOfNotNull(
        cnScoreGuide,
        extremismRiskGuide,
        saraPartnerScoreGuide,
        saraOtherScoreGuide,
        ospScoreGuide,
        ospDcIccCombinationGuide,
        ogrsScoreGuide,
        ovpGuide,
        ogpGuide,
        pnaGuide,
        rsrGuide,
        roshLevel?.toString(),
      ).sorted()
    }

  companion object {

    fun fromEntity(riskConsideration: RiskConsideration): RiskConsiderationDto = RiskConsiderationDto(
      id = riskConsideration.id,
      cnScoreGuide = riskConsideration.cnScoreGuide,
      extremismRiskGuide = riskConsideration.extremismRiskGuide,
      saraPartnerScoreGuide = riskConsideration.saraPartnerScoreGuide,
      saraOtherScoreGuide = riskConsideration.saraOtherScoreGuide,
      ospScoreGuide = riskConsideration.ospScoreGuide,
      ospDcIccCombinationGuide = riskConsideration.ospDcIccCombinationGuide,
      ogrsScoreGuide = riskConsideration.ogrsScoreGuide,
      ovpGuide = riskConsideration.ovpGuide,
      ogpGuide = riskConsideration.ogpGuide,
      pnaGuide = riskConsideration.pnaGuide,
      rsrGuide = riskConsideration.rsrGuide,
      roshLevel = riskConsideration.roshLevel,
    )
  }
}
