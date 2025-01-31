package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethod
import java.util.UUID

data class DeliveryMethodDto(
  val id: UUID,
  val description: String? = null,
  val deliveryMethodSettings: List<DeliveryMethodSettingDto> = mutableListOf(),
) {
  companion object {
    fun fromEntity(deliveryMethod: DeliveryMethod): DeliveryMethodDto {
      return DeliveryMethodDto(
        id = deliveryMethod.id,
        description = deliveryMethod.deliveryMethodDescription,
        deliveryMethodSettings = deliveryMethod.deliveryMethodSettings.map {
          DeliveryMethodSettingDto.fromEntity(it)
        },
      )
    }
  }
}
