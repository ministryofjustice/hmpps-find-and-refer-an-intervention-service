package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "delivery_location", schema = "public")
open class DeliveryLocation(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @Column(name = "provider_name", length = Integer.MAX_VALUE)
  open var providerName: String,

  @NotNull
  @Column(name = "contact", length = Integer.MAX_VALUE)
  open var contact: String,

  @NotNull
  @Column(name = "pdu_establishments", length = Integer.MAX_VALUE)
  open var pduEstablishments: String,
)
