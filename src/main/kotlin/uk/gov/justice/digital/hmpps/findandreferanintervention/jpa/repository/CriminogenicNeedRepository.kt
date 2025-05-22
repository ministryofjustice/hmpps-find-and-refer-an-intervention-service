package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeed
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface CriminogenicNeedRepository : JpaRepository<CriminogenicNeed, UUID> {
  fun findByIntervention(intervention: InterventionCatalogue): List<CriminogenicNeed>?
  fun deleteAllByIntervention(intervention: InterventionCatalogue)
}
