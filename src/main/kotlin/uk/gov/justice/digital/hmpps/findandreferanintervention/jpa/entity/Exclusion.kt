package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

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
open class Exclusion {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @Column(name = "min_remaining_sentence_duration_guide", length = Integer.MAX_VALUE)
  open var minRemainingSentenceDurationGuide: String? = null

  @Column(name = "remaining_license_community_order_guide", length = Integer.MAX_VALUE)
  open var remainingLicenseCommunityOrderGuide: String? = null

  @Column(name = "alcohol_drug_problem_guide", length = Integer.MAX_VALUE)
  open var alcoholDrugProblemGuide: String? = null

  @Column(name = "mental_health_problem_guide", length = Integer.MAX_VALUE)
  open var mentalHealthProblemGuide: String? = null

  @Column(name = "other_preferred_method_guide", length = Integer.MAX_VALUE)
  open var otherPreferredMethodGuide: String? = null

  @Column(name = "same_type_rule_guide", length = Integer.MAX_VALUE)
  open var sameTypeRuleGuide: String? = null

  @Column(name = "schedule_frequency_guide", length = Integer.MAX_VALUE)
  open var scheduleFrequencyGuide: String? = null

  @NotNull
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "intervention_id", nullable = false)
  open var intervention: InterventionCatalogue? = null
}
