package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType

interface InterventionCatalogueFilterRepository {

  @EntityGraph(
    attributePaths =
    [
      "personalEligibility",
      "riskConsideration",
      "criminogenicNeeds",
      "deliveryLocations",
      "deliveryMethods",
      "eligibleOffences",
      "enablingInterventions",
      "excludedOffences",
      "possibleOutcomes",
      "specialEducationalNeeds",
      "interventions",
    ],
  )
  fun findAllInterventionCatalogueByCriteria(
    pageable: Pageable,
    interventionTypes: List<InterventionType>?,
  ): Page<InterventionCatalogue>
}
