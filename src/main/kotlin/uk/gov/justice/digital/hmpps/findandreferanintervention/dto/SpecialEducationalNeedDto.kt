package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull

data class SpecialEducationalNeedDto(
  @field:NotNull val learningDisabilityCateredFor: Boolean? = false,
  val equivalentNonLdcProgrammeGuide: String? = null,
)
