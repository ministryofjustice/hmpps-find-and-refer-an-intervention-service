package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "organisation", schema = "public")
open class Organisation(
  @NotNull
  @Id
  @Column(name = "organisation_id")
  open var id: UUID,

  @NotNull
  @Column(name = "code", length = Integer.MAX_VALUE)
  open var code: String,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @Nullable
  @Column(name = "gender", length = Integer.MAX_VALUE)
  open var gender: String? = null,
)
