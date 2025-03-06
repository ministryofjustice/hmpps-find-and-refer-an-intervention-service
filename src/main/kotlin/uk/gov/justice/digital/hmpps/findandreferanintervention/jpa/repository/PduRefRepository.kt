package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PduRef

interface PduRefRepository : JpaRepository<PduRef, String> {
  fun findPduRefById(id: String): PduRef?
}
