package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethodSetting
import java.util.*

interface DeliveryMethodSettingRepository : JpaRepository<DeliveryMethodSetting, UUID> {
  fun findByDeliveryMethodId(deliveryMethodId: UUID): List<DeliveryMethodSetting>?
}
