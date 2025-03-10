package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.NpsRegion

data class PccRegionDto(
  val id: String,
  val name: String,
  val npsRegion: NpsRegion,
  val pduRefs: MutableSet<PduRefDto> = mutableSetOf(),
  val dynamicFrameworkContracts: MutableSet<DynamicFrameworkContractDto> = mutableSetOf(),
)
