package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PduRef
import java.util.*

interface PduRefRepository : JpaRepository<PduRef, String> {
  fun findByName(name: String?): PduRef?
}
