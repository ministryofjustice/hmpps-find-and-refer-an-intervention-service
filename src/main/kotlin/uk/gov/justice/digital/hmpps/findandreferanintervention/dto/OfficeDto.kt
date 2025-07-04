package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.util.UUID

data class OfficeDto(
  val id: UUID,
  val referral: ReferralDto,
  val officeName: String,
  val contactEmail: String,
  val createdAt: String,
  val createdByUser: String,
  val deletedAt: String?,
)
