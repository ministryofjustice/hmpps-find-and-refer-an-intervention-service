package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Dummy

interface DummyRepository : JpaRepository<Dummy, Int> {
  fun findByDummyId(dummyId: Int): Dummy?
}
