package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "service_category", schema = "public")
open class ServiceCategory {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @Column(name = "created")
  open var created: OffsetDateTime? = null

  @NotNull
  @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
  open var name: String? = null
}