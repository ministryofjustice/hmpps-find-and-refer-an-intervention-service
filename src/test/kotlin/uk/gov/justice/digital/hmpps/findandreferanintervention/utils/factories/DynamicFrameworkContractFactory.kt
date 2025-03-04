package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.ContractType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DynamicFrameworkContract
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.NpsRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PccRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.ServiceProvider
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

class DynamicFrameworkContractFactory(em: TestEntityManager? = null) : EntityFactory(em)

private val testLocalDate: LocalDate = LocalDate.of(2025, 1, 1)

fun DynamicFrameworkContractFactory.create(
  id: UUID = UUID.randomUUID(),
  primeProvider: ServiceProvider = ServiceProvider("POSITIVE_STEPS", "Positive Steps"),
  startDate: LocalDate = testLocalDate,
  endDate: LocalDate = testLocalDate.plusYears(2),
  npsRegion: NpsRegion? = null,
  pccRegion: PccRegion? = null,
  allowsFemale: Boolean = true,
  allowsMale: Boolean = true,
  minAge: Int? = null,
  maxAge: Int? = null,
  contractReference: String = "freddy.neptune@gmail.com",
  contractType: ContractType = ContractType(UUID.randomUUID(), "Accommodation", "ACC"),
  referralStartDate: LocalDate = testLocalDate,
  referralEndAt: OffsetDateTime? = null,
) = save(
  DynamicFrameworkContract(
    id,
    primeProvider,
    startDate,
    endDate,
    npsRegion,
    pccRegion,
    allowsFemale,
    allowsMale,
    minAge,
    maxAge,
    contractReference,
    contractType,
    referralStartDate,
    referralEndAt,
  ),
)
