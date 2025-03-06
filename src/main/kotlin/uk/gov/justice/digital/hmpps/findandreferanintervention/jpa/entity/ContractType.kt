package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "contract_type", schema = "public")
open class ContractType(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @Size(max = 10)
  @NotNull
  @Column(name = "code", length = 10)
  open var code: String,

  @NotNull
  @ManyToMany
  @JoinTable(
    name = "contract_type_service_category",
    joinColumns = [JoinColumn(name = "contract_type_id")],
    inverseJoinColumns = [JoinColumn(name = "service_category_id")],
  )
  open var serviceCategories: MutableSet<ServiceCategory> = mutableSetOf(),
)
