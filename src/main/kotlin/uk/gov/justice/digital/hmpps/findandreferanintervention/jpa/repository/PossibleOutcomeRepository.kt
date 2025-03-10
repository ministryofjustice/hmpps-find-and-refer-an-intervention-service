package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PossibleOutcome
import java.util.*

interface PossibleOutcomeRepository : JpaRepository<PossibleOutcome, UUID>
