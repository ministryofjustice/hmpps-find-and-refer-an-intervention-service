package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

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
  open var name: String? = null,
)
