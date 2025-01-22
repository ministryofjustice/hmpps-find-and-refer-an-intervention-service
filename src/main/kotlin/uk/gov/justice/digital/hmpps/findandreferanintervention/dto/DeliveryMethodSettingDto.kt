package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethodSetting
import java.util.UUID

data class DeliveryMethodSettingDto(
  @NotNull
  val id: UUID? = null,
  @NotNull
  val setting: DeliveryMethodSetting.SettingType? = null,
) {
  companion object {
    fun fromEntity(deliveryMethodSetting: DeliveryMethodSetting): DeliveryMethodSettingDto {
      return DeliveryMethodSettingDto(
        id = deliveryMethodSetting.id,
        setting = deliveryMethodSetting.setting,
      )
    }
  }
}
