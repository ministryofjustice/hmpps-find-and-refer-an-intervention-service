package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.PccRegionDto

@Entity
@Table(name = "pcc_region", schema = "public")
open class PccRegion(
  @NotNull
  @Id
  @Size(max = 1)
  @Column(name = "id", length = 1)
  open var id: String,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @NotNull
  @ManyToOne
  @JoinColumn(name = "nps_region_id", referencedColumnName = "id")
  open var npsRegion: NpsRegion,

  @NotNull
  @OneToMany(mappedBy = "pccRegion")
  open var pduRefs: MutableSet<PduRef> = mutableSetOf(),
)

fun PccRegion.toDto(): PccRegionDto = PccRegionDto(
  id = this.id,
  name = this.name,
  pduRefs = this.pduRefs.map { it.toDto() }.toMutableSet(),
)

fun PccRegion.getPduRefsForPccRegion(): List<PduRef> = pduRefs.map { it }
fun PccRegion.getPduRefsNamesForPccRegion(): List<String> = pduRefs.map { it.name }
