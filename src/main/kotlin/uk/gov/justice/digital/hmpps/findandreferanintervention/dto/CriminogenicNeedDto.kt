package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeed
import java.util.UUID

data class CriminogenicNeedDto(val id: UUID, val need: String) {
  companion object {
    fun fromEntity(criminogenicNeed: CriminogenicNeed): CriminogenicNeedDto = CriminogenicNeedDto(id = criminogenicNeed.id, need = criminogenicNeed.need.name)
  }
}
