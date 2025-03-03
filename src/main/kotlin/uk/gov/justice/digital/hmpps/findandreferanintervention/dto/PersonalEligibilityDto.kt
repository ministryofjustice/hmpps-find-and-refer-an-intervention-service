package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.util.UUID

data class PersonalEligibilityDto(
  val id: UUID,
  val minAge: Int?,
  val maxAge: Int?,
  val males: Boolean,
  val females: Boolean,
)
