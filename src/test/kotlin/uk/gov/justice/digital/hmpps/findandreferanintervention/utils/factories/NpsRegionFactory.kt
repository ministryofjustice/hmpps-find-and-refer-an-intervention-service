package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.NpsRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PccRegion

class NpsRegionFactory(em: TestEntityManager? = null) : EntityFactory(em)

private val pccRegionFactory = PccRegionFactory()

fun NpsRegionFactory.create(
  id: String = "A",
  name: String = "North East",
  pccRegions: MutableSet<PccRegion> = mutableSetOf(pccRegionFactory.create()),
): NpsRegion = save(
  NpsRegion(
    id,
    name,
    pccRegions,
  ),
)
