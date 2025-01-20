package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "dynamic_framework_contract", schema = "public")
open class DynamicFrameworkContract {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "prime_provider_id", nullable = false)
  open var primeProvider: ServiceProvider? = null

  @NotNull
  @Column(name = "start_date", nullable = false)
  open var startDate: LocalDate? = null

  @NotNull
  @Column(name = "end_date", nullable = false)
  open var endDate: LocalDate? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nps_region_id")
  open var npsRegion: NpsRegion? = null

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pcc_region_id")
  open var pccRegion: PccRegion? = null

  @NotNull
  @ColumnDefault("true")
  @Column(name = "allows_female", nullable = false)
  open var allowsFemale: Boolean? = false

  @NotNull
  @ColumnDefault("true")
  @Column(name = "allows_male", nullable = false)
  open var allowsMale: Boolean? = false

  @NotNull
  @ColumnDefault("18")
  @Column(name = "minimum_age", nullable = false)
  open var minimumAge: Int? = null

  @Column(name = "maximum_age")
  open var maximumAge: Int? = null

  @Size(max = 30)
  @NotNull
  @Column(name = "contract_reference", nullable = false, length = 30)
  open var contractReference: String? = null

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "contract_type_id", nullable = false)
  open var contractType: ContractType? = null

  @NotNull
  @ColumnDefault("'2021-01-01'")
  @Column(name = "referral_start_date", nullable = false)
  open var referralStartDate: LocalDate? = null

  @Column(name = "referral_end_at")
  open var referralEndAt: OffsetDateTime? = null
}
