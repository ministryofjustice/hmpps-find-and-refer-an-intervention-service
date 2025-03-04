package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.util.UUID

data class CourseDto(
  val id: UUID,
  val name: String,
  val description: String? = null,
  val alternateName: String? = null,
  val identifier: String,
  val audience: String? = null,
  val audienceColour: String? = null,
  val version: Long = 0,
  val displayOnProgrammeDirectory: Boolean = false,
  val intensity: String? = null,
  val prerequisite: List<PrerequisiteDto> = mutableListOf(),
  val offering: List<OfferingDto> = mutableListOf(),
)
