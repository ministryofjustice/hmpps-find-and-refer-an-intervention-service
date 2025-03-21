package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility
import java.util.UUID

interface PersonalEligibilityRepository : JpaRepository<PersonalEligibility, UUID> {
  fun findByIntervention(intervention: InterventionCatalogue): PersonalEligibility?
}
