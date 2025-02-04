package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethodSetting
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import java.util.UUID

data class DeliveryMethodSettingDto(
  val id: UUID,
  val setting: SettingType,
) {
  companion object {
    fun fromEntity(deliveryMethodSetting: DeliveryMethodSetting): DeliveryMethodSettingDto = DeliveryMethodSettingDto(
      id = deliveryMethodSetting.id,
      setting = deliveryMethodSetting.setting,
    )
  }
}
