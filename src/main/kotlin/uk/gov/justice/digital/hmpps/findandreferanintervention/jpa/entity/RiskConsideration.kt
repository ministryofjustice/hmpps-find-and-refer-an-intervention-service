package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType
import java.util.UUID

@Entity
@Table(name = "risk_consideration", schema = "public")
open class RiskConsideration(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @Column(name = "cn_score_guide", length = Integer.MAX_VALUE)
  open var cnScoreGuide: String?,

  @Nullable
  @Column(name = "extremism_risk_guide", length = Integer.MAX_VALUE)
  open var extremismRiskGuide: String?,

  @Nullable
  @Column(name = "sara_partner_score_guide", length = Integer.MAX_VALUE)
  open var saraPartnerScoreGuide: String?,

  @Nullable
  @Column(name = "sara_other_score_guide", length = Integer.MAX_VALUE)
  open var saraOtherScoreGuide: String?,

  @Nullable
  @Column(name = "osp_score_guide", length = Integer.MAX_VALUE)
  open var ospScoreGuide: String?,

  @Nullable
  @Column(name = "osp_dc_icc_combination_guide", length = Integer.MAX_VALUE)
  open var ospDcIccCombinationGuide: String?,

  @Nullable
  @Column(name = "ogrs_score_guide", length = Integer.MAX_VALUE)
  open var ogrsScoreGuide: String?,

  @Nullable
  @Column(name = "ovp_guide", length = Integer.MAX_VALUE)
  open var ovpGuide: String?,

  @Nullable
  @Column(name = "ogp_guide", length = Integer.MAX_VALUE)
  open var ogpGuide: String?,

  @Nullable
  @Column(name = "pna_guide", length = Integer.MAX_VALUE)
  open var pnaGuide: String?,

  @Nullable
  @Column(name = "rsr_guide", length = Integer.MAX_VALUE)
  open var rsrGuide: String?,

  @Column(name = "rosh_level", columnDefinition = "rosh_levels")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var roshLevel: RoshLevel?,

  @Nullable
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var intervention: InterventionCatalogue?,
)

enum class RoshLevel {
  LOW,
  MEDIUM,
  HIGH,
  VERY_HIGH,
}
