package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface InterventionRepository :
  JpaRepository<InterventionCatalogue, UUID>,
  InterventionFilterRepository {
  fun findInterventionCatalogueById(id: UUID): InterventionCatalogue
}
