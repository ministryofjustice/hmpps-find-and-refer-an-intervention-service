package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethodSetting
import java.io.Serializable

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethodSetting}
 */
data class DeliveryMethodSettingDto(
  @field:NotNull val deliveryMethod: DeliveryMethodDto? = null,
  val setting: DeliveryMethodSetting.SettingType? = null,
) : Serializable