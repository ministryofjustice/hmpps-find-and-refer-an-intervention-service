package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SpecialEducationalNeed}
 */
data class SpecialEducationalNeedDto(
  @field:NotNull val learningDisabilityCateredFor: Boolean? = false,
  val equivalentNonLdcProgrammeGuide: String? = null,
) : Serializable
