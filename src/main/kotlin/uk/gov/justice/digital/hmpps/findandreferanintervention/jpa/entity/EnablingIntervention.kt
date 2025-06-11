package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "enabling_intervention", schema = "public")
open class EnablingIntervention(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @Column(name = "enabling_intervention_detail", length = Integer.MAX_VALUE)
  open var enablingInterventionDetail: String? = null,

  @Nullable
  @Column(name = "convicted_for_offence_type_guide", length = Integer.MAX_VALUE)
  open var convictedForOffenceTypeGuide: String? = null,

  @Nullable
  @OneToOne
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var intervention: InterventionCatalogue?,
)
