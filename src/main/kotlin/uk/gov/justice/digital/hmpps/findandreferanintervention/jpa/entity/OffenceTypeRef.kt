package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "offence_type_ref", schema = "public")
open class OffenceTypeRef(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,
)
