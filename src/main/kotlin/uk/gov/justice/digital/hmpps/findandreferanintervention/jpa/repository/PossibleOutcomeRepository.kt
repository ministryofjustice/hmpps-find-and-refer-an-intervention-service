package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PossibleOutcome
import java.util.UUID

interface PossibleOutcomeRepository : JpaRepository<PossibleOutcome, UUID> {
  fun findByIntervention(intervention: InterventionCatalogue): PossibleOutcome?
}
