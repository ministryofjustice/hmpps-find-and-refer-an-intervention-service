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
@Table(name = "eligible_offence", schema = "public")
open class EligibleOffence {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offence_type_id")
  open var offenceType: OffenceTypeRef? = null

  @Column(name = "victim_type", length = Integer.MAX_VALUE)
  open var victimType: String? = null

  @Column(name = "motivation", length = Integer.MAX_VALUE)
  open var motivation: String? = null
}
