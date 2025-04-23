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
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.PossibleOutcomeDto
import java.util.UUID

@Entity
@Table(name = "possible_outcome", schema = "public")
open class PossibleOutcome(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @Column(name = "outcome", length = Integer.MAX_VALUE)
  open var outcome: String,

  @Nullable
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var intervention: InterventionCatalogue?,
)

fun PossibleOutcome.toDto(): PossibleOutcomeDto = PossibleOutcomeDto(
  id = this.id,
  outcome = this.outcome,
)
