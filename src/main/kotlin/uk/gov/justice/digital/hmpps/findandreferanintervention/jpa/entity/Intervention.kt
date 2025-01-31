package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "intervention", schema = "public")
open class Intervention(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "dynamic_framework_contract_id")
  open var dynamicFrameworkContract: DynamicFrameworkContract,

  @NotNull
  @Column(name = "created_at")
  open var createdAt: OffsetDateTime,

  @NotNull
  @Column(name = "title", length = Integer.MAX_VALUE)
  open var title: String,

  @NotNull
  @Column(name = "description", length = Integer.MAX_VALUE)
  open var description: String,

  @NotNull
  @ColumnDefault("'__no_data__'")
  @Column(name = "incoming_referral_distribution_email", length = Integer.MAX_VALUE)
  open var incomingReferralDistributionEmail: String,

  @ManyToMany
  @JoinTable(
    name = "intervention_catalogue_map",
    joinColumns = [JoinColumn(name = "intervention_id")],
    inverseJoinColumns = [JoinColumn(name = "intervention_catalogue_id")],
  )
  open var interventionCatalogues: MutableSet<InterventionCatalogue> = mutableSetOf(),
)
