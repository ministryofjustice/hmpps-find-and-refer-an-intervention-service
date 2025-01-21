package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RiskConsideration
import java.io.Serializable
import java.util.UUID

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RiskConsideration}
 */
data class RiskConsiderationDto(
  val id: UUID? = null,
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
  val roshLevel: RiskConsideration.RoshLevel? = null,
) : Serializable