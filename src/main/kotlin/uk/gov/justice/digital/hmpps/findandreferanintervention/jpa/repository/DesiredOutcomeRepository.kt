package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DesiredOutcome
import java.util.*

interface DesiredOutcomeRepository : JpaRepository<DesiredOutcome, UUID>
