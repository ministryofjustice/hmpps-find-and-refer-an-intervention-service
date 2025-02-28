package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.util.UUID

data class OfferingDto(
  val id: UUID,
  val organisationId: String,
  val contactEmail: String,
  val secondaryContactEmail: String? = null,
  val withdrawn: Boolean = false,
  val referable: Boolean = true,
  val version: Long = 0,
)
