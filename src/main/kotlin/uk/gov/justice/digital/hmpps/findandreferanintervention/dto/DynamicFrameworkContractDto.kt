package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class DynamicFrameworkContractDto(
  val id: UUID,
  val startDate: LocalDate,
  val endDate: LocalDate,
  val npsRegion: NpsRegionDto? = null,
  val pccRegion: PccRegionDto? = null,
  val allowsFemale: Boolean,
  val allowsMale: Boolean,
  val minimumAge: Int? = null,
  val maximumAge: Int? = null,
  val contractReference: String,
  val referralStartDate: LocalDate,
  val referralEndAt: OffsetDateTime? = null,
)
