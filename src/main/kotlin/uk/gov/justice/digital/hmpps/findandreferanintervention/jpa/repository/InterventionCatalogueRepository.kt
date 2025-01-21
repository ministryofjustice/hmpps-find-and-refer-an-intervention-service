package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface InterventionCatalogueRepository : JpaRepository<InterventionCatalogue, UUID> {
  @EntityGraph(attributePaths = ["criminogenic_need", "delivery_location", "delivery_method", "eligible_offence", "enabling_intervention", "excluded_offence", "intervention", "personal_eligibility", "possible_outcome", "risk_consideration", "special_education_needs"])
  override fun findAll(pageable: Pageable): Page<InterventionCatalogue>
}
