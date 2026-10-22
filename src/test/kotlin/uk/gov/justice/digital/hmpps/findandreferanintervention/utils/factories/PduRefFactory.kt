package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PccRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PduRef

class PduRefFactory(em: TestEntityManager? = null) : EntityFactory(em)

private val deliveryLocationFactory = DeliveryLocationFactory()

fun PduRefFactory.create(
  id: String = "redcar-cleveland-and-middlesbrough",
  name: String = "Redcar, Cleveland and Middlesbrough",
  pccRegions: PccRegion,
  deliveryLocations: MutableSet<DeliveryLocation> = mutableSetOf(),
): PduRef = save(
  PduRef(
    id,
    name,
    pccRegions,
    deliveryLocations,
  ),
)
