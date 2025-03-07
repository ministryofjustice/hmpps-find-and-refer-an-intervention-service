package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.OffenceTypeRef
import java.util.*

interface OffenceTypeRefRepository : JpaRepository<OffenceTypeRef, UUID> {
  fun findByName(name: String): OffenceTypeRef?
}
