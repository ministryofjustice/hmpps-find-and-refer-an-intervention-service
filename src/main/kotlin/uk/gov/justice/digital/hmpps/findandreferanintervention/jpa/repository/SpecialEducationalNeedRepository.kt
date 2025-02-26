package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SpecialEducationalNeed
import java.util.*

interface SpecialEducationalNeedRepository : JpaRepository<SpecialEducationalNeed, UUID>
