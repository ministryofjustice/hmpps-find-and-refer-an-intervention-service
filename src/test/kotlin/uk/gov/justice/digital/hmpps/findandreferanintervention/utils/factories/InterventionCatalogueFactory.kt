package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.AuthUser
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeed
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethod
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethodSetting
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EligibleOffence
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EnablingIntervention
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.ExcludedOffence
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Exclusion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Intervention
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PossibleOutcome
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RiskConsideration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SpecialEducationalNeed
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

class InterventionCatalogueFactory(em: TestEntityManager? = null) : EntityFactory(em)

private val authUserFactory = AuthUserFactory()
private val criminogenicNeedFactory = CriminogenicNeedFactory()
private val deliveryMethodFactory = DeliveryMethodFactory()
private val exclusionFactory = ExclusionFactory()
private val riskConsiderationFactory = RiskConsiderationFactory()
private val personalEligibilityFactory = PersonalEligibilityFactory()
private val specialEducationNeedFactory = SpecialEducationNeedFactory()
private val enablingInterventionFactory = EnablingInterventionFactory()
private val interventionFactory = InterventionFactory()

private val testLocalDate: LocalDate = LocalDate.of(2025, 1, 1)
private val testOffsetDateTime: OffsetDateTime = OffsetDateTime.of(testLocalDate, LocalTime.NOON, ZoneOffset.UTC)

fun InterventionCatalogueFactory.create(
  id: UUID = UUID.randomUUID(),
  name: String = "Finance, Benefit & Debt",
  shortDescription: String = "Service to addresses immediate financial needs",
  longDescription: MutableList<String> = mutableListOf("In custody FBD services focus on addressing immediate financial needs that arise from being in custody such as freezing debts and stopping benefits."),
  topic: String = "",
  sessionDetail: String = "5 x One-to-one sessions and 25 group sessions",
  commencementDate: LocalDate = testLocalDate,
  terminationDate: LocalDate = testLocalDate,
  created: OffsetDateTime = testOffsetDateTime,
  createdBy: AuthUser = authUserFactory.create(),
  lastModified: OffsetDateTime = testOffsetDateTime,
  lastModifiedBy: AuthUser = authUserFactory.create(),
  interventionType: InterventionType = InterventionType.CRS,
  timeToComplete: String = "At least 6 months",
  reasonForReferral: String = "Lifestyle and associate",
  criminogenicNeeds: MutableSet<CriminogenicNeed> = criminogenicNeedFactory.createSet(),
  deliveryLocations: MutableSet<DeliveryLocation> = mutableSetOf(),
  deliveryMethod: DeliveryMethod = deliveryMethodFactory.create(),
  deliveryMethodSettings: MutableSet<DeliveryMethodSetting> = mutableSetOf(),
  eligibleOffences: MutableSet<EligibleOffence> = mutableSetOf(),
  enablingInterventions: EnablingIntervention? = enablingInterventionFactory.create(),
  excludedOffences: MutableSet<ExcludedOffence> = mutableSetOf(),
  exclusion: Exclusion = exclusionFactory.create(),
  interventions: MutableSet<Intervention> = mutableSetOf(interventionFactory.create()),
  personalEligibility: PersonalEligibility = personalEligibilityFactory.create(),
  possibleOutcomes: MutableSet<PossibleOutcome> = mutableSetOf(),
  riskConsideration: RiskConsideration = riskConsiderationFactory.create(),
  specialEducationalNeeds: SpecialEducationalNeed = specialEducationNeedFactory.create(),
): InterventionCatalogue = save(
  InterventionCatalogue(
    id,
    name,
    shortDescription,
    longDescription,
    topic,
    sessionDetail,
    commencementDate,
    terminationDate,
    created,
    createdBy,
    lastModified,
    lastModifiedBy,
    interventionType,
    timeToComplete,
    reasonForReferral,
    criminogenicNeeds,
    deliveryLocations,
    deliveryMethod,
    deliveryMethodSettings,
    eligibleOffences,
    enablingInterventions,
    excludedOffences,
    exclusion,
    interventions,
    personalEligibility,
    possibleOutcomes,
    riskConsideration,
    specialEducationalNeeds,
  ),
)

fun InterventionCatalogueFactory.createDto(
  id: UUID = UUID.randomUUID(),
  name: String = "Finance, Benefit & Debt",
  shortDescription: String = "Service to addresses immediate financial needs",
  longDescription: MutableList<String> = mutableListOf("In custody FBD services focus on addressing immediate financial needs that arise from being in custody such as freezing debts and stopping benefits."),
  topic: String = "",
  sessionDetail: String = "5 x One-to-one sessions and 25 group sessions",
  commencementDate: LocalDate = testLocalDate,
  terminationDate: LocalDate = testLocalDate,
  created: OffsetDateTime = testOffsetDateTime,
  createdBy: AuthUser = authUserFactory.create(),
  lastModified: OffsetDateTime = testOffsetDateTime,
  lastModifiedBy: AuthUser = authUserFactory.create(),
  interventionType: InterventionType = InterventionType.CRS,
  timeToComplete: String = "At least 6 months",
  reasonForReferral: String = "Lifestyle and associate",
  criminogenicNeeds: MutableSet<CriminogenicNeed> = criminogenicNeedFactory.createSet(),
  deliveryLocations: MutableSet<DeliveryLocation> = mutableSetOf(),
  deliveryMethod: DeliveryMethod = deliveryMethodFactory.create(),
  deliveryMethodSettings: MutableSet<DeliveryMethodSetting> = mutableSetOf(),
  eligibleOffences: MutableSet<EligibleOffence> = mutableSetOf(),
  enablingIntervention: EnablingIntervention = enablingInterventionFactory.create(),
  excludedOffences: MutableSet<ExcludedOffence> = mutableSetOf(),
  exclusion: Exclusion = exclusionFactory.create(),
  interventions: MutableSet<Intervention> = mutableSetOf(),
  personalEligibility: PersonalEligibility = personalEligibilityFactory.create(),
  possibleOutcomes: MutableSet<PossibleOutcome> = mutableSetOf(),
  riskConsideration: RiskConsideration = riskConsiderationFactory.create(),
  specialEducationalNeeds: SpecialEducationalNeed = specialEducationNeedFactory.create(),
): InterventionCatalogueDto = this.create(
  id,
  name,
  shortDescription,
  longDescription,
  topic,
  sessionDetail,
  commencementDate,
  terminationDate,
  created,
  createdBy,
  lastModified,
  lastModifiedBy,
  interventionType,
  timeToComplete,
  reasonForReferral,
  criminogenicNeeds,
  deliveryLocations,
  deliveryMethod,
  deliveryMethodSettings,
  eligibleOffences,
  enablingIntervention,
  excludedOffences,
  exclusion,
  interventions,
  personalEligibility,
  possibleOutcomes,
  riskConsideration,
  specialEducationalNeeds,
).toDto()
