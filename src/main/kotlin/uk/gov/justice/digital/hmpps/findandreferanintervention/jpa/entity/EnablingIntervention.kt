package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "enabling_intervention", schema = "public")
open class EnablingIntervention {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @Column(name = "enabling_intervention_detail", length = Integer.MAX_VALUE)
  open var enablingInterventionDetail: String? = null
}
