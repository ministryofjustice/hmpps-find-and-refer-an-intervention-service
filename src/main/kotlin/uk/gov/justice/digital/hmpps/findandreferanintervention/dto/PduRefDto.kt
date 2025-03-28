package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

data class PduRefDto(
  val id: String,
  val pduName: String,
  val pccRegion: PccRegionDto
)
