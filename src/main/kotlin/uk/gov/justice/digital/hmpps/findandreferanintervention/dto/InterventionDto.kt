package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Intervention}
 */
data class InterventionDto(
  @field:NotNull val title: String? = null,
  @field:NotNull val description: String? = null,
  val interventionCatalogues: MutableSet<InterventionCatalogueDto> = mutableSetOf(),
) : Serializable
