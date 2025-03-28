package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

data class PccRegionDto(
  val id: String,
  val name: String,
  val dynamicFrameworkContracts: MutableSet<DynamicFrameworkContractDto> = mutableSetOf(),
  val npsRegion: NpsRegionDto,
)
