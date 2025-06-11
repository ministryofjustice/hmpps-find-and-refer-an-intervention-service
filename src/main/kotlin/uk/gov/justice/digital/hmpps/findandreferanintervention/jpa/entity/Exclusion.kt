package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "exclusion", schema = "public")
open class Exclusion(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @Column(name = "min_remaining_sentence_duration_guide", length = Integer.MAX_VALUE)
  open var minRemainingSentenceDurationGuide: String? = null,

  @Nullable
  @Column(name = "remaining_license_community_order_guide", length = Integer.MAX_VALUE)
  open var remainingLicenseCommunityOrderGuide: String? = null,

  @Nullable
  @Column(name = "alcohol_drug_problem_guide", length = Integer.MAX_VALUE)
  open var alcoholDrugProblemGuide: String? = null,

  @Nullable
  @Column(name = "mental_health_problem_guide", length = Integer.MAX_VALUE)
  open var mentalHealthProblemGuide: String? = null,

  @Nullable
  @Column(name = "other_preferred_method_guide", length = Integer.MAX_VALUE)
  open var otherPreferredMethodGuide: String? = null,

  @Nullable
  @Column(name = "same_type_rule_guide", length = Integer.MAX_VALUE)
  open var sameTypeRuleGuide: String? = null,

  @Nullable
  @Column(name = "schedule_frequency_guide", length = Integer.MAX_VALUE)
  open var scheduleFrequencyGuide: String? = null,

  @Nullable
  @Column(name = "not_allowed_if_eligible_for_another_intervention_guide", length = Integer.MAX_VALUE)
  open var notAllowedIfEligibleForAnotherInterventionGuide: String? = null,

  @Nullable
  @Column(name = "literacy_level_guide", length = Integer.MAX_VALUE)
  open var literacyLevelGuide: String? = null,

  @Nullable
  @Column(name = "convicted_for_offence_type_guide", length = Integer.MAX_VALUE)
  open var convictedForOffenceTypeGuide: String? = null,

  @Nullable
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var intervention: InterventionCatalogue?,
)
