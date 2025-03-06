package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DynamicFrameworkContract
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.NpsRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PduRef

data class PccRegionDto(
  val id: String,
  val name: String,
  val npsRegion: NpsRegion,
  val pduRef: MutableSet<PduRef> = mutableSetOf(),
  val dynamicFrameworkContracts: MutableSet<DynamicFrameworkContract> = mutableSetOf(),
)
