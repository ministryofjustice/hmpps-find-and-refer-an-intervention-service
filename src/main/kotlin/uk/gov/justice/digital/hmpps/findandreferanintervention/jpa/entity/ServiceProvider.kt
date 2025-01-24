package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "service_provider", schema = "public")
open class ServiceProvider {
  @Id
  @Size(max = 30)
  @Column(name = "id", nullable = false, length = 30)
  open var id: String? = null

  @NotNull
  @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
  open var name: String? = null
}
