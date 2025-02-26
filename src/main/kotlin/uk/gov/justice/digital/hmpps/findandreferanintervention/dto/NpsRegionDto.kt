package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

data class NpsRegionDto(
  val id: String,
  val name: String,
  val pccRegions: MutableSet<PccRegionDto> = mutableSetOf(),
)
