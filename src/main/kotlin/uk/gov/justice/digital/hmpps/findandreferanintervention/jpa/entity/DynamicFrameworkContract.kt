package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
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
open class DynamicFrameworkContract(
  @NotNull
  @Id
  @Column(name = "id")
  var id: UUID,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "prime_provider_id")
  var primeProvider: ServiceProvider,

  @NotNull
  @Column(name = "start_date")
  var startDate: LocalDate,

  @NotNull
  @Column(name = "end_date")
  var endDate: LocalDate,

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nps_region_id")
  var npsRegion: NpsRegion? = null,

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pcc_region_id")
  var pccRegion: PccRegion? = null,

  @NotNull
  @ColumnDefault("true")
  @Column(name = "allows_female")
  var allowsFemale: Boolean,

  @NotNull
  @ColumnDefault("true")
  @Column(name = "allows_male")
  var allowsMale: Boolean,

  @NotNull
  @ColumnDefault("18")
  @Column(name = "minimum_age")
  var minimumAge: Int? = 18,

  @NotNull
  @ColumnDefault("120")
  @Column(name = "maximum_age")
  var maximumAge: Int? = 120,

  @Size(max = 30)
  @NotNull
  @Column(name = "contract_reference", length = 30)
  var contractReference: String,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "contract_type_id")
  var contractType: ContractType,

  @NotNull
  @ColumnDefault("'2021-01-01'")
  @Column(name = "referral_start_date")
  var referralStartDate: LocalDate,

  @Nullable
  @Column(name = "referral_end_at")
  var referralEndAt: OffsetDateTime,
)
