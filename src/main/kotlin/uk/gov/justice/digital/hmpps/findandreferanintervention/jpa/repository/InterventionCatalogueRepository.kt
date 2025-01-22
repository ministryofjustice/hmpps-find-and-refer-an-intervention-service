package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface InterventionCatalogueRepository : JpaRepository<InterventionCatalogue, UUID> {
  @EntityGraph(attributePaths = ["criminogenicNeeds", "deliveryLocations", "deliveryMethods", "eligibleOffences", "enablingInterventions", "excludedOffences", "exclusion", "interventions", "personalEligibility", "possibleOutcomes", "riskConsideration", "specialEducationalNeeds"])
  override fun findAll(pageable: Pageable): Page<InterventionCatalogue>
}
