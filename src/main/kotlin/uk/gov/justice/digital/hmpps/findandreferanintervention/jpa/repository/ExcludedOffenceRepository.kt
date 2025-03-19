package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.ExcludedOffence
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface ExcludedOffenceRepository : JpaRepository<ExcludedOffence, UUID> {
  fun findByIntervention(intervention: InterventionCatalogue): List<ExcludedOffence>?
}
