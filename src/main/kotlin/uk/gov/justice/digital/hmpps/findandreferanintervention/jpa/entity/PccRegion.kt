package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "pcc_region", schema = "public")
open class PccRegion {
  @Id
  @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
  open var id: String? = null

  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String? = null

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "nps_region", nullable = false)
  open var npsRegion: NpsRegion? = null
}