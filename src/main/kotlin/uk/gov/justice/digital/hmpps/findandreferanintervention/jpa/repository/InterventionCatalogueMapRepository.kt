package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogueMap
import java.util.UUID

interface InterventionCatalogueMapRepository : JpaRepository<InterventionCatalogueMap, UUID> {
  fun deleteByInterventionCatalogueIdAndInterventionId(interventionCatalogueId: UUID, interventionId: UUID)
  fun findByInterventionIdAndInterventionCatalogueId(interventionId: UUID, interventionCatalogueId: UUID): InterventionCatalogueMap?
}
