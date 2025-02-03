package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "eligible_ethnicity", schema = "public")
open class EligibleEthnicity(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ethnicity_id")
  open var ethnicity: EthnicityRef? = null,

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "personal_eligibility_id")
  open var personalEligibility: PersonalEligibility? = null,
)
