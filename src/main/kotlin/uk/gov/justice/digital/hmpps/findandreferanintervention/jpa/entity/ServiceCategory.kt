package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "service_category", schema = "public")
open class ServiceCategory(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @Column(name = "created")
  open var created: OffsetDateTime? = null,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @NotNull
  @ManyToMany
  @JoinTable(
    name = "contract_type_service_category",
    joinColumns = [JoinColumn(name = "service_category_id")],
    inverseJoinColumns = [JoinColumn(name = "contract_type_id")],
  )
  open var contractTypes: MutableSet<ContractType> = mutableSetOf(),
)
