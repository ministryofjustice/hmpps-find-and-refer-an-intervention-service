package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

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
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.util.UUID

@Entity
@Table(name = "risk_consideration", schema = "public")
open class RiskConsideration {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @Column(name = "cn_score_guide", length = Integer.MAX_VALUE)
  open var cnScoreGuide: String? = null

  @Column(name = "extremism_risk_guide", length = Integer.MAX_VALUE)
  open var extremismRiskGuide: String? = null

  @Column(name = "sara_partner_score_guide", length = Integer.MAX_VALUE)
  open var saraPartnerScoreGuide: String? = null

  @Column(name = "sara_other_score_guide", length = Integer.MAX_VALUE)
  open var saraOtherScoreGuide: String? = null

  @Column(name = "osp_score_guide", length = Integer.MAX_VALUE)
  open var ospScoreGuide: String? = null

  @Column(name = "osp_dc_icc_combination_guide", length = Integer.MAX_VALUE)
  open var ospDcIccCombinationGuide: String? = null

  @Column(name = "ogrs_score_guide", length = Integer.MAX_VALUE)
  open var ogrsScoreGuide: String? = null

  @Column(name = "ovp_guide", length = Integer.MAX_VALUE)
  open var ovpGuide: String? = null

  @Column(name = "ogp_guide", length = Integer.MAX_VALUE)
  open var ogpGuide: String? = null

  @Column(name = "pna_guide", length = Integer.MAX_VALUE)
  open var pnaGuide: String? = null

  @Column(name = "rsr_guide", length = Integer.MAX_VALUE)
  open var rsrGuide: String? = null

  @Column(name = "rosh_level", columnDefinition = "rosh_levels")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var roshLevel: RoshLevel? = null

  @NotNull
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "intervention_id", nullable = false)
  open var intervention: InterventionCatalogue? = null

  enum class RoshLevel {
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH,
  }
}
