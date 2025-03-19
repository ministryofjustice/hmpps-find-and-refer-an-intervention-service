package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EligibleOffence
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface EligibleOffenceRepository : JpaRepository<EligibleOffence, UUID> {
  fun findByIntervention(intervention: InterventionCatalogue): List<EligibleOffence>?
}
