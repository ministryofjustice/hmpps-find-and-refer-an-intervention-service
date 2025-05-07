package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethod
import java.util.UUID

data class DeliveryMethodDto(
  val id: UUID,
  val attendanceType: String? = null,
  val description: String? = null,
  val deliveryFormat: String? = null,
) {
  companion object {
    fun fromEntity(deliveryMethod: DeliveryMethod): DeliveryMethodDto = DeliveryMethodDto(
      id = deliveryMethod.id,
      attendanceType = deliveryMethod.attendanceType,
      description = deliveryMethod.deliveryMethodDescription,
      deliveryFormat = deliveryMethod.deliveryFormat,
    )
  }
}
