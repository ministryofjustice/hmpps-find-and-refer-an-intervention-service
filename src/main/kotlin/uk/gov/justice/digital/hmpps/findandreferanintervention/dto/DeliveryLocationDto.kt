package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

data class DeliveryLocationDto(
  val id: UUID,
  val providerName: String,
  val contact: String,
  val pduRef: PduRefDto,
  val intervention: InterventionCatalogue,
)
