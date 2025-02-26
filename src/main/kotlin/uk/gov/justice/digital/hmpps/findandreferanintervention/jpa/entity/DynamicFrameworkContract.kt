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
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.DynamicFrameworkContractDto
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "dynamic_framework_contract", schema = "public")
open class DynamicFrameworkContract(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "prime_provider_id")
  open var primeProvider: ServiceProvider,

  @NotNull
  @Column(name = "start_date")
  open var startDate: LocalDate,

  @NotNull
  @Column(name = "end_date")
  open var endDate: LocalDate,

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nps_region_id", referencedColumnName = "id")
  open var npsRegion: NpsRegion? = null,

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pcc_region_id", referencedColumnName = "id")
  open var pccRegion: PccRegion? = null,

  @NotNull
  @ColumnDefault("true")
  @Column(name = "allows_female")
  open var allowsFemale: Boolean,

  @NotNull
  @ColumnDefault("true")
  @Column(name = "allows_male")
  open var allowsMale: Boolean,

  @Nullable
  @Column(name = "minimum_age")
  open var minimumAge: Int? = null,

  @Nullable
  @Column(name = "maximum_age")
  open var maximumAge: Int? = null,

  @Size(max = 30)
  @NotNull
  @Column(name = "contract_reference", length = 30)
  open var contractReference: String,

  @NotNull
  @ManyToOne
  @JoinColumn(name = "contract_type_id")
  open var contractType: ContractType,

  @NotNull
  @ColumnDefault("'2021-01-01'")
  @Column(name = "referral_start_date")
  open var referralStartDate: LocalDate,

  @Nullable
  @Column(name = "referral_end_at")
  open var referralEndAt: OffsetDateTime,
)

fun DynamicFrameworkContract.toDto(): DynamicFrameworkContractDto = DynamicFrameworkContractDto(
  id = this.id,
  startDate = this.startDate,
  endDate = this.endDate,
  npsRegion = this.npsRegion?.toDto(),
  pccRegion = this.pccRegion?.toDto(),
  allowsFemale = this.allowsFemale,
  allowsMale = this.allowsMale,
  minimumAge = this.minimumAge,
  maximumAge = this.maximumAge,
  contractReference = this.contractReference,
  referralStartDate = this.referralStartDate,
  referralEndAt = this.referralEndAt,
)
