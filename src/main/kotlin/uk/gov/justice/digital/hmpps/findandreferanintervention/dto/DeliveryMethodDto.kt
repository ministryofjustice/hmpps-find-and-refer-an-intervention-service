package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethod
import java.util.UUID

data class DeliveryMethodDto(
  val id: UUID,
  val attendanceType: String? = null,
  val description: String? = null,
  val deliveryFormat: String? = null,
  val deliveryMethodSettings: List<DeliveryMethodSettingDto> = mutableListOf(),
) {
  companion object {
    fun fromEntity(deliveryMethod: DeliveryMethod): DeliveryMethodDto {
      return DeliveryMethodDto(
        id = deliveryMethod.id,
        attendanceType = deliveryMethod.attendanceType,
        description = deliveryMethod.deliveryMethodDescription,
        deliveryFormat = deliveryMethod.deliveryFormat,
        deliveryMethodSettings = deliveryMethod.deliveryMethodSettings.map {
          DeliveryMethodSettingDto.fromEntity(it)
        },
      )
    }
  }
}
