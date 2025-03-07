package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeedRef
import java.util.*

interface CriminogenicNeedRefRepository : JpaRepository<CriminogenicNeedRef, UUID> {
  fun findByName(name: String?): CriminogenicNeedRef?
}
