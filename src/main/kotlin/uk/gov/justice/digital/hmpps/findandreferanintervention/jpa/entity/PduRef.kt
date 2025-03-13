package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.PduRefDto

@Entity
@Table(name = "pdu_ref", schema = "public")
open class PduRef(
  @NotNull
  @Id
  @Column(name = "id", length = Integer.MAX_VALUE)
  open var id: String,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pcc_region_id")
  open var pccRegion: PccRegion? = null,

  @NotNull
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "pduRef")
  open var deliveryLocations: MutableSet<DeliveryLocation> = mutableSetOf(),
)

fun PduRef.toDto(): PduRefDto = PduRefDto(
  id = this.id,
  pduName = this.name,
)
