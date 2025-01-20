package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "intervention", schema = "public")
open class Intervention {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @NotNull
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "dynamic_framework_contract_id", nullable = false)
  open var dynamicFrameworkContract: DynamicFrameworkContract? = null

  @NotNull
  @Column(name = "created_at", nullable = false)
  open var createdAt: OffsetDateTime? = null

  @NotNull
  @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
  open var title: String? = null

  @NotNull
  @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
  open var description: String? = null

  @NotNull
  @ColumnDefault("'__no_data__'")
  @Column(name = "incoming_referral_distribution_email", nullable = false, length = Integer.MAX_VALUE)
  open var incomingReferralDistributionEmail: String? = null
}
