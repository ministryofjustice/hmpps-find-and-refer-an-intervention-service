package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RiskConsideration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RoshLevel
import java.util.UUID

class RiskConsiderationFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun RiskConsiderationFactory.create(
  id: UUID = UUID.randomUUID(),
  cnScoreGuide: String? = null,
  extremismRiskGuide: String? = null,
  saraPartnerScoreGuide: String? = null,
  saraOtherScoreGuide: String? = null,
  ospScoreGuide: String? = null,
  ospDcIccCombinationGuide: String? = null,
  ogrsScoreGuide: String? = null,
  ovpGuide: String? = null,
  ogpGuide: String? = null,
  pnaGuide: String? = null,
  rsrGuide: String? = null,
  roshLevel: RoshLevel? = null,
  intervention: InterventionCatalogue? = null,
): RiskConsideration = save(
  RiskConsideration(
    id,
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
    roshLevel,
    intervention,
  ),
)
