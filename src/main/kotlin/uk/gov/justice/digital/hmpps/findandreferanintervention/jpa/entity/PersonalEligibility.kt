package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "personal_eligibility", schema = "public")
open class PersonalEligibility(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @Column(name = "min_age")
  open var minAge: Int? = null,

  @Nullable
  @Column(name = "max_age")
  open var maxAge: Int? = null,

  @NotNull
  @Column(name = "males")
  open var males: Boolean,

  @NotNull
  @Column(name = "females")
  open var females: Boolean,

  @Nullable
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var intervention: InterventionCatalogue?,
)
