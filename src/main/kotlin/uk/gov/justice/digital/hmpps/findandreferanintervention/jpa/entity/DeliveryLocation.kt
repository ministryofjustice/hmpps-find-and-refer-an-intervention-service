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
@Table(name = "delivery_location", schema = "public")
open class DeliveryLocation {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id")
  open var intervention: InterventionCatalogue? = null

  @NotNull
  @Column(name = "provider_name", nullable = false, length = Integer.MAX_VALUE)
  open var providerName: String? = null

  @NotNull
  @Column(name = "contact", nullable = false, length = Integer.MAX_VALUE)
  open var contact: String? = null

  @NotNull
  @Column(name = "pdu_establishments", nullable = false, length = Integer.MAX_VALUE)
  open var pduEstablishments: String? = null
}
