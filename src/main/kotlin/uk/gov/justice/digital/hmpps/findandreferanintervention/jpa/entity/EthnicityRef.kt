package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "ethnicity_ref", schema = "public")
open class EthnicityRef {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @NotNull
  @Column(name = "broad_group", nullable = false, length = Integer.MAX_VALUE)
  open var broadGroup: String? = null

  @NotNull
  @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
  open var name: String? = null
}
