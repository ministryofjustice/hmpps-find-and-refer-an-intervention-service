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

  @NotNull
  @OneToOne(mappedBy = "interventionCatalogue", fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "intervention_id")
  open var intervention: InterventionCatalogue,
) {
  // This is to avoid stack overflow issues with the bi-directional association with referral
  override fun toString(): String = "Exclusion(id=$id, minRemainingSentenceDurationGuide=$minRemainingSentenceDurationGuide,remainingLicenseCommunityOrderGuide=$remainingLicenseCommunityOrderGuide,alcoholDrugProblemGuide=$alcoholDrugProblemGuide,alcoholDrugProblemGuide=$alcoholDrugProblemGuide,otherPreferredMethodGuide=$otherPreferredMethodGuide,sameTypeRuleGuide=$sameTypeRuleGuide,scheduleFrequencyGuide=$scheduleFrequencyGuide,intervention=$intervention )"

  override fun hashCode(): Int = id.hashCode()

  override fun equals(other: Any?): Boolean {
    if (other == null || other !is Exclusion) {
      return false
    }

    return id == other.id
  }
}
