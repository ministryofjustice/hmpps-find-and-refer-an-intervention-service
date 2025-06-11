package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EnablingIntervention
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface EnablingInterventionRepository : JpaRepository<EnablingIntervention, UUID> {
  fun findByIntervention(intervention: InterventionCatalogue): EnablingIntervention?
}
