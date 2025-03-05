package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface InterventionCatalogueRepository :
  JpaRepository<InterventionCatalogue, UUID>,
  InterventionCatalogueFilterRepository {
  fun findInterventionCatalogueById(id: UUID): InterventionCatalogue?
}
