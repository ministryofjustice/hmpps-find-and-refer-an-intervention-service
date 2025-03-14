package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EligibleOffence
import java.util.*

interface EligibleOffenceRepository : JpaRepository<EligibleOffence, UUID>
