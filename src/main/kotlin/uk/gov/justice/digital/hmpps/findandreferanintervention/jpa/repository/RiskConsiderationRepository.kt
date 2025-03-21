package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RiskConsideration
import java.util.UUID

interface RiskConsiderationRepository : JpaRepository<RiskConsideration, UUID> {
  fun findByIntervention(intervention: InterventionCatalogue): RiskConsideration?
}
