package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.DeliveryLocationDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toDto
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
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pdu_ref_id", referencedColumnName = "id")
  open var pduRef: PduRef,

  @NotNull
  @ManyToOne
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var intervention: InterventionCatalogue,
)

fun DeliveryLocation.toDto(): DeliveryLocationDto = DeliveryLocationDto(
  id = this.id,
  pduRef = this.pduRef.toDto(),
  providerName = this.providerName,
  contact = this.contact,
  intervention = this.intervention.toDto(),
)
