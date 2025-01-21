package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.io.Serializable

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethod}
 */
data class DeliveryMethodDto(val deliveryMethodSettings: MutableSet<DeliveryMethodSettingDto> = mutableSetOf()) :
  Serializable