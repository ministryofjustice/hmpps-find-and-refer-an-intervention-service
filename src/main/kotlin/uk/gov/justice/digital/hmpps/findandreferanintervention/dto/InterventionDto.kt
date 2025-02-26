package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.util.UUID

data class InterventionDto(
  val id: UUID,
  val title: String,
  val description: String,
  val dynamicFrameworkContract: DynamicFrameworkContractDto,
)
