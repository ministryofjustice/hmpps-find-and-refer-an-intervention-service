package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PccRegion

data class PduRefDto(
  val id: String,
  val name: String,
  val pccRegionDto: PccRegion,
  val deliveryLocations: MutableSet<DeliveryLocation>,
)
