package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.util.UUID

data class OrganisationDto(
  val id: UUID,
  val code: String,
  val name: String,
  val gender: String? = null,
  val category: String? = null,
  val county: String? = null,
)
