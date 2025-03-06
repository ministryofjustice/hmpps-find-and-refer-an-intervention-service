package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DynamicFrameworkContract
import java.util.UUID

data class InterventionDto(
  val id: UUID,
  val title: String,
  val description: String,
  val dynamicFrameworkContract: DynamicFrameworkContract,
)
