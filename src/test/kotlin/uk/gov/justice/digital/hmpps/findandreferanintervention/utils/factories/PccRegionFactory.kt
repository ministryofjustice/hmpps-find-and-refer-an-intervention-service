package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.NpsRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PccRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PduRef

class PccRegionFactory(em: TestEntityManager? = null) : EntityFactory(em)

private val pduRefFactory = PduRefFactory()

fun PccRegionFactory.create(
  id: String = "cleveland",
  name: String = "Cleveland",
  npsRegion: NpsRegion? = null,
  pduRef: MutableSet<PduRef> = mutableSetOf(),
): PccRegion = save(
  PccRegion(
    id,
    name,
    npsRegion ?: NpsRegion("A", "North East"),
    pduRef,
  ),
)
