package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "intervention_catalogue", schema = "public")
open class InterventionCatalogue {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @NotNull
  @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
  open var name: String? = null

  @NotNull
  @Column(name = "short_description", nullable = false, length = Integer.MAX_VALUE)
  open var shortDescription: String? = null

  @NotNull
  @Column(name = "long_description", nullable = false, length = Integer.MAX_VALUE)
  open var longDescription: String? = null

  @NotNull
  @Column(name = "topic", nullable = false, length = Integer.MAX_VALUE)
  open var topic: String? = null

  @NotNull
  @Column(name = "session_detail", nullable = false, length = Integer.MAX_VALUE)
  open var sessionDetail: String? = null

  @NotNull
  @Column(name = "commencement_date", nullable = false)
  open var commencementDate: LocalDate? = null

  @NotNull
  @Column(name = "termination_date", nullable = false)
  open var terminationDate: LocalDate? = null

  @NotNull
  @Column(name = "created", nullable = false)
  open var created: OffsetDateTime? = null

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by", nullable = false)
  open var createdBy: AuthUser? = null

  @Column(name = "last_modified")
  open var lastModified: OffsetDateTime? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "last_modified_by")
  open var lastModifiedBy: AuthUser? = null

  @Column(name = "int_type", columnDefinition = "intervention_type not null")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var intType: InterventionType? = null

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var criminogenicNeeds: MutableSet<CriminogenicNeed> = mutableSetOf()

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var deliveryLocations: MutableSet<DeliveryLocation> = mutableSetOf()

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var deliveryMethods: MutableSet<DeliveryMethod> = mutableSetOf()

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var eligibleOffences: MutableSet<EligibleOffence> = mutableSetOf()

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var enablingInterventions: MutableSet<EnablingIntervention> = mutableSetOf()

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var excludedOffences: MutableSet<ExcludedOffence> = mutableSetOf()

  @OneToOne(mappedBy = "intervention")
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var exclusion: Exclusion? = null

  @ManyToMany
  @JoinTable(
    name = "intervention_catalogue_map",
    joinColumns = [JoinColumn(name = "intervention_catalogue_id")],
    inverseJoinColumns = [JoinColumn(name = "intervention_id")],
  )
  open var interventions: MutableSet<Intervention> = mutableSetOf()

  @OneToOne(mappedBy = "intervention")
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var personalEligibility: PersonalEligibility? = null

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var possibleOutcomes: MutableSet<PossibleOutcome> = mutableSetOf()

  @OneToOne(mappedBy = "intervention")
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var riskConsideration: RiskConsideration? = null

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var specialEducationalNeeds: MutableSet<SpecialEducationalNeed> = mutableSetOf()

  enum class InterventionType {
    SI,
    ACP,
    CRS,
  }
}
