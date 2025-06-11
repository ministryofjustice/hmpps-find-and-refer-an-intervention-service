package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EnablingIntervention

// Excludes any properties that have null values when creating dto.
@JsonInclude(JsonInclude.Include.NON_NULL)
data class EnablingInterventionDto(
  val enablingInterventionDetail: String? = null,
  val convictedForOffenceTypeGuide: String? = null,
)

fun EnablingIntervention.toDto(): EnablingInterventionDto = EnablingInterventionDto(
  enablingInterventionDetail = this.enablingInterventionDetail,
  convictedForOffenceTypeGuide = this.convictedForOffenceTypeGuide,
)
