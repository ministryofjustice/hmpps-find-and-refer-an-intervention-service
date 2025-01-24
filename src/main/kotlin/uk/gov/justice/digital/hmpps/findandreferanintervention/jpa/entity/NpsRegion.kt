package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Size

@Entity
@Table(name = "nps_region", schema = "public")
open class NpsRegion {
  @Id
  @Size(max = 1)
  @Column(name = "id", nullable = false, length = 1)
  open var id: String? = null

  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String? = null
}
