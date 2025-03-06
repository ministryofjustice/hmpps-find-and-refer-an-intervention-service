package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType

interface InterventionFilterRepository {

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
    allowsFemales: Boolean?,
    allowsMales: Boolean?,
    interventionTypes: List<InterventionType>?,
    settingType: SettingType?,
    programmeName: String?,
  ): Page<InterventionCatalogue>
}
