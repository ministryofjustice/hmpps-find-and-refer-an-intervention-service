package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "dynamic_framework_contract_sub_contractor", schema = "public")
open class DynamicFrameworkContractSubContractor(
  @NotNull
  @Id
  open var id: UUID,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "subcontractor_provider_id")
  open var subcontractorProvider: ServiceProvider,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "dynamic_framework_contract_id")
  open var dynamicFrameworkContract: DynamicFrameworkContract,
)
