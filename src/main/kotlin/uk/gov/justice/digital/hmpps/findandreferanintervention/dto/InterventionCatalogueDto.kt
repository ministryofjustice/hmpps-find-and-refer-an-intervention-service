package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.io.Serializable

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue}
 */
data class InterventionCatalogueDto(
  val intType: InterventionCatalogue.InterventionType? = null,
  val criminogenicNeeds: MutableSet<CriminogenicNeedDto> = mutableSetOf(),
  val deliveryMethods: MutableSet<DeliveryMethodDto> = mutableSetOf(),
  val personalEligibility: PersonalEligibilityDto? = null,
  val riskConsideration: RiskConsiderationDto? = null,
  val specialEducationalNeeds: MutableSet<SpecialEducationalNeedDto> = mutableSetOf(),
) : Serializable