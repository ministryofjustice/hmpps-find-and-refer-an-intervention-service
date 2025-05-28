package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EnablingIntervention
import java.util.UUID

data class EnablingInterventionDto(
  val id: UUID,
  val enablingInterventionDetail: String? = null,
)

fun EnablingIntervention.toDto(): EnablingInterventionDto = EnablingInterventionDto(
  id = this.id,
  enablingInterventionDetail = this.enablingInterventionDetail,
)
