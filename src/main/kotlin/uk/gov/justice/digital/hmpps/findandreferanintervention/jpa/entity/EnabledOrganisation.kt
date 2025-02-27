package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "enabled_organisation", schema = "public")
open class EnabledOrganisation(
  @NotNull
  @Id
  @Column(name = "code", length = Integer.MAX_VALUE)
  open var code: String,

  @NotNull
  @Column(name = "description", length = Integer.MAX_VALUE)
  open var description: String,
)
