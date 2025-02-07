package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
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
open class InterventionCatalogue(
  @Id open var id: UUID,
  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @Nullable
  @Column(name = "short_description", length = Integer.MAX_VALUE)
  open var shortDescription: String,

  @Nullable
  @Column(name = "long_description", length = Integer.MAX_VALUE)
  open var longDescription: String? = null,

  @Nullable
  @Column(name = "topic", length = Integer.MAX_VALUE)
  open var topic: String? = null,

  @Nullable
  @Column(name = "session_detail", length = Integer.MAX_VALUE)
  open var sessionDetail: String? = null,

  @Nullable
  @Column(name = "commencement_date")
  open var commencementDate: LocalDate? = null,

  @Nullable
  @Column(name = "termination_date")
  open var terminationDate: LocalDate? = null,

  @NotNull
  @Column(name = "created")
  open var created: OffsetDateTime,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "created_by")
  open var createdBy: AuthUser,

  @Nullable
  @Column(name = "last_modified")
  open var lastModified: OffsetDateTime? = null,

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "last_modified_by")
  open var lastModifiedBy: AuthUser? = null,

  @NotNull
  @Column(name = "int_type")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var interventionType: InterventionType,

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var criminogenicNeeds: MutableSet<CriminogenicNeed> = mutableSetOf(),

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var deliveryLocations: MutableSet<DeliveryLocation> = mutableSetOf(),

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var deliveryMethods: MutableSet<DeliveryMethod> = mutableSetOf(),

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var eligibleOffences: MutableSet<EligibleOffence> = mutableSetOf(),

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var enablingInterventions: MutableSet<EnablingIntervention> = mutableSetOf(),

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var excludedOffences: MutableSet<ExcludedOffence> = mutableSetOf(),

  @Nullable
  @OneToOne
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var exclusion: Exclusion? = null,

  @ManyToMany
  @JoinTable(
    name = "intervention_catalogue_map",
    joinColumns = [JoinColumn(name = "intervention_catalogue_id")],
    inverseJoinColumns = [JoinColumn(name = "intervention_id")],
  )
  open var interventions: MutableSet<Intervention> = mutableSetOf(),

  @Nullable
  @OneToOne(mappedBy = "intervention")
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var personalEligibility: PersonalEligibility? = null,

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var possibleOutcomes: MutableSet<PossibleOutcome> = mutableSetOf(),

  @Nullable
  @OneToOne(mappedBy = "intervention")
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var riskConsideration: RiskConsideration? = null,

  @Nullable
  @Column(name = "reason_for_referral", length = Integer.MAX_VALUE)
  open var reasonForReferral: String? = null,

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var specialEducationalNeeds: MutableSet<SpecialEducationalNeed> = mutableSetOf(),
) {
  // This is to avoid stack overflow issues with the bi-directional association with referral
  override fun toString(): String = "InterventionCatalogue(id=$id,name=$name,shortDescription=$shortDescription,longDescription=$longDescription, topic=$topic,sessionDetail=$sessionDetail,commencementDate=$commencementDate,terminationDate=$terminationDate, created=$created, createdBy=$createdBy, lastModified=$lastModified,lastModifiedBy=$lastModifiedBy, intervetionType=$interventionType,criminogenicNeeds=$criminogenicNeeds),deliveryLocations=$deliveryLocations,deliveryMethods=$deliveryMethods, eligibleOffences=$eligibleOffences, enablingInterventions=$enablingInterventions,excludedOffences=$excludedOffences, exclusion=$exclusion, interventions=$interventions, personalEligibility=$personalEligibility,possibleOutcomes=$possibleOutcomes, riskConsideration=$riskConsideration,reasonsForReferral=$reasonsForReferral, specialEducationNeeds=$specialEducationalNeeds"

  override fun hashCode(): Int = id.hashCode()

  override fun equals(other: Any?): Boolean {
    if (other == null || other !is InterventionCatalogue) {
      return false
    }

    return id == other.id
  }
}

enum class InterventionType {
  SI,
  ACP,
  CRS,
}
