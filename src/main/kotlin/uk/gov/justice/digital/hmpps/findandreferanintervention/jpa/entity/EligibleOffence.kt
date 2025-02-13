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
@Table(name = "eligible_offence", schema = "public")
open class EligibleOffence(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offence_type_id")
  open var offenceType: OffenceTypeRef? = null,

  @Nullable
  @Column(name = "victim_type", length = Integer.MAX_VALUE)
  open var victimType: String? = null,

  @Nullable
  @Column(name = "motivation", length = Integer.MAX_VALUE)
  open var motivation: String? = null,

  @Nullable
  @ManyToOne
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var intervention: InterventionCatalogue?,
)
