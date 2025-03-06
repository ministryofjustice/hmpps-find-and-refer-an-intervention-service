package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.NpsRegionDto

@Entity
@Table(name = "nps_region", schema = "public")
open class NpsRegion(
  @NotNull
  @Id
  @Size(max = 1)
  @Column(name = "id", length = 1)
  open var id: String,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "npsRegion")
  open var pccRegions: MutableSet<PccRegion> = mutableSetOf(),

  @OneToMany(mappedBy = "npsRegion")
  open var dynamicFrameworkContracts: MutableSet<DynamicFrameworkContract> = mutableSetOf(),
)

fun NpsRegion.toDto(): NpsRegionDto = NpsRegionDto(
  id = this.id,
  name = this.name,
  pccRegions = this.pccRegions.map { it.toDto() }.toMutableSet(),
)
