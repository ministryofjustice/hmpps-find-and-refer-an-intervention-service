package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethod
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

class DeliveryMethodFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun DeliveryMethodFactory.create(
  id: UUID = UUID.randomUUID(),
  attendanceType: String? = "In Person and Online",
  deliveryFormat: String? = "Group or One-to-one",
  deliveryMethodDescription: String? = "Default Description",
  intervention: InterventionCatalogue? = null,
): DeliveryMethod = save(
  DeliveryMethod(
    id,
    attendanceType,
    deliveryFormat,
    deliveryMethodDescription,
    intervention,
  ),
)
