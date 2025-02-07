package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeed
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeedRef
import java.util.UUID

class CriminogenicNeedFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun CriminogenicNeedFactory.create(
  id: UUID = UUID.randomUUID(),
  criminogenicNeedRef: CriminogenicNeedRef = CriminogenicNeedRef(UUID.randomUUID(), "Relationships and Family"),
): CriminogenicNeed = save(CriminogenicNeed(id, criminogenicNeedRef))

fun CriminogenicNeedFactory.createSet(
  id: UUID = UUID.randomUUID(),
  criminogenicNeedRef: CriminogenicNeedRef = CriminogenicNeedRef(UUID.randomUUID(), "Relationships and Family"),
): MutableSet<CriminogenicNeed> = save(mutableSetOf(create(id, criminogenicNeedRef)))
