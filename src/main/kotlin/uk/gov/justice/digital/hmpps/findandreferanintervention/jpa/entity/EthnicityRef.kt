package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "ethnicity_ref", schema = "public")
open class EthnicityRef(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @Column(name = "broad_group", length = Integer.MAX_VALUE)
  open var broadGroup: String,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,
)
