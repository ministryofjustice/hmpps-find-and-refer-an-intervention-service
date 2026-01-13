package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeed
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeedRef
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

class CriminogenicNeedFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun CriminogenicNeedFactory.create(
  id: UUID = UUID.randomUUID(),
  criminogenicNeedRef: CriminogenicNeedRef = CriminogenicNeedRef(UUID.randomUUID(), "Relatives and Family"),
  intervention: InterventionCatalogue? = null,
): CriminogenicNeed = save(CriminogenicNeed(id, criminogenicNeedRef, intervention))

fun CriminogenicNeedFactory.createSet(
  id: UUID = UUID.randomUUID(),
  criminogenicNeedRef: CriminogenicNeedRef = CriminogenicNeedRef(UUID.randomUUID(), "Relatives and Family"),
): MutableSet<CriminogenicNeed> = save(mutableSetOf(create(id, criminogenicNeedRef)))
