package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Referral
import java.util.UUID

interface ReferralRepository : JpaRepository<Referral, UUID> {

  fun findReferralById(referralId: UUID): Referral?

  fun findByPersonReferenceAndInterventionNameAndSourcedFromReference(
    personReference: String,
    interventionName: String,
    sourcedFromReference: String,
  ): Referral?
}
