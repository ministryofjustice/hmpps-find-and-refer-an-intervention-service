package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.util.UUID

data class SpecialEducationalNeedDto(
  val id: UUID,
  val learningDisabilityCateredFor: Boolean,
  val equivalentNonLdcProgrammeGuide: String,
)
