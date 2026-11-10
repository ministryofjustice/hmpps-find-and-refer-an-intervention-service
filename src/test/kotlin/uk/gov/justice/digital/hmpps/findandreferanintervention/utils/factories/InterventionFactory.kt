package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DynamicFrameworkContract
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Intervention
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

class InterventionFactory(em: TestEntityManager? = null) : EntityFactory(em)

private val testLocalDate: LocalDate = LocalDate.of(2025, 1, 1)
private val testOffsetDateTime: OffsetDateTime = OffsetDateTime.of(testLocalDate, LocalTime.NOON, ZoneOffset.UTC)
private val dynamicFrameworkContractFactory: DynamicFrameworkContractFactory = DynamicFrameworkContractFactory()

fun InterventionFactory.create(
  id: UUID = UUID.randomUUID(),
  dynamicFrameworkContract: DynamicFrameworkContract = dynamicFrameworkContractFactory.create(),
  createdAt: OffsetDateTime = testOffsetDateTime,
  title: String = "Sheffield Housing Services",
  description: String = "Inclusive housing for South Yorkshire",
  incomingReferralDistributionEmail: String = "shs-incoming@provider.example.com",
): Intervention = save(
  Intervention(
    id,
    dynamicFrameworkContract,
    createdAt,
    title,
    description,
    incomingReferralDistributionEmail,
  ),
)
