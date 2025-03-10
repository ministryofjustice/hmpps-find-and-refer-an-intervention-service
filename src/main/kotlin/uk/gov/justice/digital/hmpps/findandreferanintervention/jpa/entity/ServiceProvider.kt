package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

typealias AuthGroupID = String

@Entity
@Table(name = "service_provider", schema = "public")
open class ServiceProvider(
  @NotNull
  @Id
  @Size(max = 30)
  @Column(name = "id", length = 30)
  open var id: AuthGroupID,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,
)
