package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "eligible_ethnicity", schema = "public")
open class EligibleEthnicity {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ethnicity_id")
  open var ethnicity: EthnicityRef? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "personal_eligibility_id")
  open var personalEligibility: PersonalEligibility? = null
}
