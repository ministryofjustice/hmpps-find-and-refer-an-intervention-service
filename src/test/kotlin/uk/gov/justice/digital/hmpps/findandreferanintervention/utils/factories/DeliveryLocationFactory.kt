package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PduRef
import java.util.UUID

class DeliveryLocationFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun DeliveryLocationFactory.create(
  id: UUID = UUID.randomUUID(),
  providerName: String = "Ingenius Ltd",
  contact: String = "testyMcTestFace@gmail.com",
  pduRef: PduRef,
  intervention: InterventionCatalogue,
): DeliveryLocation = save(
  DeliveryLocation(
    id,
    providerName,
    contact,
    pduRef,
    intervention,
  ),
)
