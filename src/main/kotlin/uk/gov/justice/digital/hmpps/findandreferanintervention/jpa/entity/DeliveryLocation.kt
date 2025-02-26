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
import java.util.UUID

@Entity
@Table(name = "delivery_location", schema = "public")
open class DeliveryLocation(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

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
  intervention = this.intervention,
)
