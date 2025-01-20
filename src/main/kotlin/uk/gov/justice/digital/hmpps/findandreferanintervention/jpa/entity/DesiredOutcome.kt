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
@Table(name = "desired_outcome", schema = "public")
open class DesiredOutcome {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @NotNull
  @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
  open var description: String? = null

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "service_category_id", nullable = false)
  open var serviceCategory: ServiceCategory? = null
}
