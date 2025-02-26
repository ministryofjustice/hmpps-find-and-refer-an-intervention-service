package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.ComplexityLevel
import java.util.*

interface ComplexityLevelRepository : JpaRepository<ComplexityLevel, UUID>
