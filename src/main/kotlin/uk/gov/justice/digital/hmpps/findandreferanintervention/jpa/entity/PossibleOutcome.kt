package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

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
@Table(name = "possible_outcome", schema = "public")
open class PossibleOutcome {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id")
  open var intervention: InterventionCatalogue? = null

  @NotNull
  @Column(name = "outcome", nullable = false, length = Integer.MAX_VALUE)
  open var outcome: String? = null
}
