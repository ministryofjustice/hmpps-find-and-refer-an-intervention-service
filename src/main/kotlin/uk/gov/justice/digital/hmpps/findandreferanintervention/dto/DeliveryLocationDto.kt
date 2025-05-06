package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.util.UUID

data class DeliveryLocationDto(
  val id: UUID,
  val providerName: String?,
  val contact: String?,
  val pduRef: PduRefDto,
  val intervention: InterventionCatalogueDto,
)
