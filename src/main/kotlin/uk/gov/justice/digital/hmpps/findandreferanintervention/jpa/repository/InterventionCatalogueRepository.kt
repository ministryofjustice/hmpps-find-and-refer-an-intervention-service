package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

@Repository
interface InterventionCatalogueRepository :
  JpaRepository<InterventionCatalogue, UUID>,
  JpaSpecificationExecutor<InterventionCatalogue> {
  fun findInterventionCatalogueById(id: UUID): InterventionCatalogue?

  override fun findAll(
    specification: Specification<InterventionCatalogue>,
    pageable: Pageable,
  ): Page<InterventionCatalogue>
}
