package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import java.util.UUID

data class CrsInterventionDetailsDto(
  val interventionCatalogueId: UUID,
  val interventionType: InterventionType,
  val region: String,
  val location: String,
  val serviceCategory: String,
  val provider: String,
  val minAge: Int?,
  val maxAge: Int?,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val description: String,
)
