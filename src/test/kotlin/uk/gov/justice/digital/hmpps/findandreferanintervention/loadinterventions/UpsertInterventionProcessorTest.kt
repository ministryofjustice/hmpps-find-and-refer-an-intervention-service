package uk.gov.justice.digital.hmpps.findandreferanintervention.loadinterventions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.AdditionalAnswers
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.DeliveryLocationDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.DeliveryMethodDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.EligibleOffenceDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.ExcludedOffencesDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.ExclusionDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.InterventionCatalogueDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.InterventionCatalogueEntryDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.InterventionLoadFileReaderHelper
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.PersonalEligibilityDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.RiskConsiderationDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.SpecialEducationalNeedDefinition
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.UpsertInterventionProcessor
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeed
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeedRef
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethod
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EligibleOffence
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EnablingIntervention
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.ExcludedOffence
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Exclusion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.NpsRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.OffenceTypeRef
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PccRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PduRef
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PossibleOutcome
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RiskConsideration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RoshLevel
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SpecialEducationalNeed
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.CriminogenicNeedRefRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.CriminogenicNeedRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryLocationRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryMethodRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryMethodSettingRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.EligibleOffenceRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.EnablingInterventionRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.ExcludedOffenceRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.ExclusionRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.OffenceTypeRefRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PduRefRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PersonalEligibilityRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PossibleOutcomeRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.RiskConsiderationRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.SpecialEducationalNeedRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.InterventionCatalogueFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.create
import java.util.UUID

internal class UpsertInterventionProcessorTest {

  private val interventionCatalogueRepository = mock<InterventionCatalogueRepository>()
  private val criminogenicNeedRepository = mock<CriminogenicNeedRepository>()
  private val criminogenicNeedRefRepository = mock<CriminogenicNeedRefRepository>()
  private val deliveryLocationRepository = mock<DeliveryLocationRepository>()
  private val pduRefRepository = mock<PduRefRepository>()
  private val deliveryMethodRepository = mock<DeliveryMethodRepository>()
  private val deliveryMethodSettingRepository = mock<DeliveryMethodSettingRepository>()
  private val eligibleOffenceRepository = mock<EligibleOffenceRepository>()
  private val offenceTypeRefRepository = mock<OffenceTypeRefRepository>()
  private val enablingInterventionRepository = mock<EnablingInterventionRepository>()
  private val excludedOffenceRepository = mock<ExcludedOffenceRepository>()
  private val exclusionRepository = mock<ExclusionRepository>()
  private val personalEligibilityRepository = mock<PersonalEligibilityRepository>()
  private val possibleOutcomeRepository = mock<PossibleOutcomeRepository>()
  private val riskConsiderationRepository = mock<RiskConsiderationRepository>()
  private val specialEducationalNeedRepository = mock<SpecialEducationalNeedRepository>()

  private val interventionCatalogueFactory = InterventionCatalogueFactory()
  private val catalogue = interventionCatalogueFactory.create()

  private val processor = UpsertInterventionProcessor(
    interventionCatalogueRepository,
    criminogenicNeedRepository,
    criminogenicNeedRefRepository,
    deliveryLocationRepository,
    pduRefRepository,
    deliveryMethodRepository,
    deliveryMethodSettingRepository,
    eligibleOffenceRepository,
    offenceTypeRefRepository,
    enablingInterventionRepository,
    excludedOffenceRepository,
    exclusionRepository,
    personalEligibilityRepository,
    possibleOutcomeRepository,
    riskConsiderationRepository,
    specialEducationalNeedRepository,
  )

  @BeforeEach
  fun setup() {
    whenever(interventionCatalogueRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg<InterventionCatalogue>())
    whenever(criminogenicNeedRepository.saveAll(anyList())).thenAnswer(AdditionalAnswers.returnsFirstArg<CriminogenicNeed>())
    whenever(criminogenicNeedRefRepository.findByName(any())).thenReturn(CriminogenicNeedRef(id = UUID.randomUUID(), name = "Default Need Type"))
    whenever(criminogenicNeedRefRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg<CriminogenicNeedRef>())
    whenever(deliveryLocationRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg<DeliveryLocation>())
    val defaultNpsRegion = NpsRegion(UUID.randomUUID().toString(), "Default NPS Region", pccRegions = mutableSetOf())
    val defaultPccRegion = PccRegion(UUID.randomUUID().toString(), "Default PCC Region", defaultNpsRegion, mutableSetOf())
    val defaultPduRef1 = PduRef(UUID.randomUUID().toString(), "Default PDU Ref", defaultPccRegion, mutableSetOf())
    val defaultPduRef2 = PduRef(UUID.randomUUID().toString(), "Default PDU Ref", defaultPccRegion, mutableSetOf())
    whenever(pduRefRepository.findByName(any())).thenReturn(defaultPduRef1).thenReturn(defaultPduRef2).thenReturn(null)
    whenever(deliveryMethodRepository.saveAll(anyList())).thenAnswer(AdditionalAnswers.returnsFirstArg<DeliveryMethod>())
    whenever(deliveryMethodSettingRepository.saveAll(anyList())).thenAnswer(AdditionalAnswers.returnsFirstArg<DeliveryMethod>())
    whenever(eligibleOffenceRepository.saveAll(anyList())).thenAnswer(AdditionalAnswers.returnsFirstArg<EligibleOffence>())
    whenever(enablingInterventionRepository.saveAll(anyList())).thenAnswer(AdditionalAnswers.returnsFirstArg<EnablingIntervention>())
    whenever(excludedOffenceRepository.saveAll(anyList())).thenAnswer(AdditionalAnswers.returnsFirstArg<ExcludedOffence>())
    whenever(offenceTypeRefRepository.findByName(any())).thenReturn(OffenceTypeRef(id = UUID.randomUUID(), name = "Default Offence Type"))
    whenever(offenceTypeRefRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg<OffenceTypeRef>())
    whenever(exclusionRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg<Exclusion>())
    whenever(personalEligibilityRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg<PersonalEligibility>())
    whenever(possibleOutcomeRepository.saveAll(anyList())).thenAnswer(AdditionalAnswers.returnsFirstArg<PossibleOutcome>())
    whenever(riskConsiderationRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg<RiskConsideration>())
    whenever(specialEducationalNeedRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg<SpecialEducationalNeed>())
  }

  @Test
  fun `Providing Json to be extracted to call process() method and create as an Intervention Catalogue Definition object to be stored into the database`() {
    val interventionCatalogueDefinitionJson =
      InterventionLoadFileReaderHelper.getResource(
        "classpath:db/interventions/interventionCatalogueDefinitions/InterventionCatalogueDefinition.json",
      )

    val interventionCatalogueDefinitions =
      ObjectMapper().readValue(interventionCatalogueDefinitionJson, object : TypeReference<InterventionCatalogueDefinition>() {})

    val result = processor.process(interventionCatalogueDefinitions)

    assertThat(result?.name).isEqualTo("Accommodation")
    assertThat(result?.interventionType).isEqualTo(InterventionType.CRS)
    assertThat(result?.personalEligibility?.minAge).isEqualTo(18)
    verify(interventionCatalogueRepository, times(2)).save(any())
  }

  @Test
  fun `Providing Json with blank Catalogue Name to be extracted as an Intervention Catalogue Definition object to be stored into the database`() {
    val interventionCatalogueDefinitionJson =
      InterventionLoadFileReaderHelper.getResource(
        "classpath:db/interventions/interventionCatalogueDefinitions/InterventionCatalogueDefinitionNoName.json",
      )

    val interventionCatalogueDefinitions =
      ObjectMapper().readValue(interventionCatalogueDefinitionJson, object : TypeReference<InterventionCatalogueDefinition>() {})

    val result = processor.process(interventionCatalogueDefinitions)

    assertThat(result).isEqualTo(null)
    verify(interventionCatalogueRepository, times(0)).save(any())
  }

  @Test
  fun `Providing Json with invalid InterventionType to be extracted as an Intervention Catalogue Definition object to be stored into the database`() {
    val interventionCatalogueDefinitionJson =
      InterventionLoadFileReaderHelper.getResource(
        "classpath:db/interventions/interventionCatalogueDefinitions/InterventionCatalogueDefinitionInvalidType.json",
      )

    val interventionCatalogueDefinitions =
      ObjectMapper().readValue(interventionCatalogueDefinitionJson, object : TypeReference<InterventionCatalogueDefinition>() {})

    val result = processor.process(interventionCatalogueDefinitions)

    assertThat(result).isEqualTo(null)
    verify(interventionCatalogueRepository, times(0)).save(any())
  }

  @Test
  fun `Providing Json to be extracted to call createInterventionCatalogue() method and create a full Intervention Catalogue Definition object to be stored into the database`() {
    val interventionCatalogueDefinitionJson =
      InterventionLoadFileReaderHelper.getResource(
        "classpath:db/interventions/interventionCatalogueDefinitions/InterventionCatalogueDefinition.json",
      )

    val interventionCatalogueDefinitions =
      ObjectMapper().readValue(interventionCatalogueDefinitionJson, object : TypeReference<InterventionCatalogueDefinition>() {})

    val result = processor.createInterventionCatalogue(interventionCatalogueDefinitions)

    assertThat(result.name).isEqualTo("Accommodation")
    assertThat(result.interventionType).isEqualTo(InterventionType.CRS)
    verify(interventionCatalogueRepository, times(2)).save(any())
  }

  @Test
  fun `Providing Json to be extracted as an Intervention Catalogue Entry Definition object to be stored into the database`() {
    val catalogueId = UUID.randomUUID()

    val interventionCatalogueEntryDefinitionJson =
      InterventionLoadFileReaderHelper.getResource(
        "classpath:db/interventions/interventionCatalogueDefinitions/InterventionCatalogueEntryDefinition.json",
      )

    val interventionCatalogueEntryDefinitions =
      ObjectMapper().readValue(interventionCatalogueEntryDefinitionJson, object : TypeReference<InterventionCatalogueEntryDefinition>() {})

    val result = processor.insertInterventionCatalogueEntry(interventionCatalogueEntryDefinitions, catalogueId)

    assertThat(result.name).isEqualTo("Accommodation")
    assertThat(result.interventionType).isEqualTo(InterventionType.CRS)
    verify(interventionCatalogueRepository, times(1)).save(any())
  }

  @Test
  fun `Providing Json to be extracted as an Array of Criminogenic Need Definition objects to be stored into the database`() {
    val criminogenicNeedDefinitionJson =
      InterventionLoadFileReaderHelper.getResource(
        "classpath:db/interventions/criminogenicNeedTestDefinitions/CriminogenicNeedDefinition.json",
      )

    val criminogenicNeedDefinitions =
      ObjectMapper().readValue(criminogenicNeedDefinitionJson, object : TypeReference<Array<String>>() {})

    val result = processor.insertCriminogenicNeeds(criminogenicNeedDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(1)
    verify(criminogenicNeedRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json with one valid and one unknown value to be extracted as an Array of Criminogenic Need Definition objects to be stored into the database`() {
    val criminogenicNeedDefinitionJson =
      InterventionLoadFileReaderHelper.getResource(
        "classpath:db/interventions/criminogenicNeedTestDefinitions/CriminogenicNeedDefinitionUnknownId.json",
      )

    val criminogenicNeedDefinitions =
      ObjectMapper().readValue(criminogenicNeedDefinitionJson, object : TypeReference<Array<String>>() {})

    whenever(criminogenicNeedRefRepository.findByName(any()))
      .thenReturn(CriminogenicNeedRef(id = UUID.randomUUID(), name = "Accommodation"))
      .thenReturn(null)

    val result = processor.insertCriminogenicNeeds(criminogenicNeedDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(2)
    verify(criminogenicNeedRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json to be extracted as an Array of Delivery Location Definition objects to be stored into the database`() {
    val deliveryLocationDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/DeliveryLocationDefinition.json")

    val deliveryLocationDefinitions =
      ObjectMapper().readValue(deliveryLocationDefinitionJson, object : TypeReference<Array<DeliveryLocationDefinition>>() {})

    val defaultNpsRegion = NpsRegion(UUID.randomUUID().toString(), "Default NPS Region", pccRegions = mutableSetOf())
    val defaultPccRegion = PccRegion(UUID.randomUUID().toString(), "Default PCC Region", defaultNpsRegion, mutableSetOf())
    val defaultPduRef = PduRef(UUID.randomUUID().toString(), "Default PDU Ref", defaultPccRegion, mutableSetOf())

    whenever(pduRefRepository.findByName(any())).thenReturn(defaultPduRef)

    val result = processor.insertDeliveryLocations(deliveryLocationDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(3)
    verify(deliveryLocationRepository, times(3)).save(any())
  }

  @Test
  fun `Providing Json with invalid PDU Reference to throw an exception when extracting as an Array of Delivery Location Definition objects`() {
    val deliveryLocationDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/DeliveryLocationDefinitionInvalidPduRef.json")

    val deliveryLocationDefinitions =
      ObjectMapper().readValue(deliveryLocationDefinitionJson, object : TypeReference<Array<DeliveryLocationDefinition>>() {})

    val exception = assertThrows<RuntimeException> {
      processor.insertDeliveryLocations(deliveryLocationDefinitions, catalogue)
    }

    assertThat(exception.message).contains("PDU for Default Provider Name 2 was not found")
    verify(deliveryLocationRepository, times(2)).save(any())
  }

  @Test
  fun `Providing Json to be extracted as an Array of Delivery Method Definition objects to be stored into the database`() {
    val deliveryMethodDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/DeliveryMethodDefinition.json")

    val deliveryMethodDefinitions =
      ObjectMapper().readValue(deliveryMethodDefinitionJson, object : TypeReference<Array<DeliveryMethodDefinition>>() {})

    val result = processor.insertDeliveryMethods(deliveryMethodDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(3)
    verify(deliveryMethodRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json to be extracted as an Array of Delivery Method Setting Definition objects to be stored into the database`() {
    val deliveryMethodDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/DeliveryMethodDefinition.json")

    val deliveryMethodSettingDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/DeliveryMethodSettingDefinition.json")

    val deliveryMethodDefinitions =
      ObjectMapper().readValue(deliveryMethodDefinitionJson, object : TypeReference<Array<DeliveryMethodDefinition>>() {})

    val deliveryMethodSettingDefinitions =
      ObjectMapper().readValue(deliveryMethodSettingDefinitionJson, object : TypeReference<Array<String>>() {})

    val deliveryMethods = processor.insertDeliveryMethods(deliveryMethodDefinitions, catalogue)
    val result = processor.insertDeliveryMethodSettings(deliveryMethodSettingDefinitions, deliveryMethods)

    assertThat(result.count()).isEqualTo(3)
    verify(deliveryMethodSettingRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json to be extracted as an Array of Eligible Offence Definition objects to be stored into the database`() {
    val eligibleOffenceDefinitionJson =
      InterventionLoadFileReaderHelper.getResource(
        "classpath:db/interventions/eligibleOffenceDefinitions/EligibleOffenceDefinition.json",
      )

    val eligibleOffenceDefinitions =
      ObjectMapper().readValue(eligibleOffenceDefinitionJson, object : TypeReference<Array<EligibleOffenceDefinition>>() {})

    val result = processor.insertEligibleOffences(eligibleOffenceDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(2)
    verify(eligibleOffenceRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json with one valid and one unknown Offence Type to be extracted as an Array of Eligible Offence Definition objects to be stored into the database`() {
    val eligibleOffenceDefinitionJson =
      InterventionLoadFileReaderHelper.getResource(
        "classpath:db/interventions/eligibleOffenceDefinitions/EligibleOffenceDefinitionUnknownId.json",
      )

    val eligibleOffenceDefinitions =
      ObjectMapper().readValue(eligibleOffenceDefinitionJson, object : TypeReference<Array<EligibleOffenceDefinition>>() {})

    whenever(offenceTypeRefRepository.findByName(any()))
      .thenReturn(OffenceTypeRef(id = UUID.randomUUID(), name = "Extremism offence"))
      .thenReturn(null)

    val result = processor.insertEligibleOffences(eligibleOffenceDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(2)
    verify(eligibleOffenceRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json to be extracted as an Array of Enabling Intervention Definition objects to be stored into the database`() {
    val enablingInterventionDefinitionsJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/EnablingInterventionDefinition.json")

    val enablingInterventionDefinitions =
      ObjectMapper().readValue(enablingInterventionDefinitionsJson, object : TypeReference<String>() {})

    val result = processor.insertEnablingInterventions(enablingInterventionDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(4)
    verify(enablingInterventionRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json to be extracted as an Array of Excluded Offence Definition objects to be stored into the database`() {
    val excludedOffenceDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/excludedOffenceDefinitions/ExcludedOffenceDefinition.json")

    val excludedOffenceDefinitions =
      ObjectMapper().readValue(excludedOffenceDefinitionJson, object : TypeReference<Array<ExcludedOffencesDefinition>>() {})

    val result = processor.insertExcludedOffences(excludedOffenceDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(2)
    verify(excludedOffenceRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json with one valid and one unknown Offence Type to be extracted as an Array of Excluded Offence Definition objects to be stored into the database`() {
    val excludedOffenceDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/excludedOffenceDefinitions/ExcludedOffenceDefinitionUnknownId.json")

    val excludedOffenceDefinitions =
      ObjectMapper().readValue(excludedOffenceDefinitionJson, object : TypeReference<Array<ExcludedOffencesDefinition>>() {})

    whenever(offenceTypeRefRepository.findByName(any()))
      .thenReturn(OffenceTypeRef(id = UUID.randomUUID(), name = "Extremism offence"))
      .thenReturn(null)

    val result = processor.insertExcludedOffences(excludedOffenceDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(2)
    verify(excludedOffenceRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json to be extracted as an Exclusion Definition object to be stored into the database`() {
    val exclusionDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/ExclusionDefinition.json")

    val exclusionDefinition =
      ObjectMapper().readValue(exclusionDefinitionJson, object : TypeReference<ExclusionDefinition>() {})

    val result = processor.insertExclusion(exclusionDefinition, catalogue)

    assertThat(result.remainingLicenseCommunityOrderGuide).isEqualTo("12 months on licence or at least 18 months on community order.")
    assertThat(result.mentalHealthProblemGuide).isEqualTo("Identified mental illness, or personality disorder")
    verify(exclusionRepository, times(1)).save(any())
  }

  @Test
  fun `Providing Json to be extracted as a Personal Eligibility Definition object to be stored into the database`() {
    val personalEligibilityDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/PersonalEligibilityDefinition.json")

    val personalEligibilityDefinition =
      ObjectMapper().readValue(personalEligibilityDefinitionJson, object : TypeReference<PersonalEligibilityDefinition>() {})

    val result = processor.insertPersonalEligibility(personalEligibilityDefinition, catalogue)

    assertThat(result.minAge).isEqualTo(18)
    verify(personalEligibilityRepository, times(1)).save(any())
  }

  @Test
  fun `Providing Json to be extracted as an Array of Possible Outcome Definition objects to be stored into the database`() {
    val possibleOutcomeDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/PossibleOutcomeDefinition.json")

    val possibleOutcomeDefinitions =
      ObjectMapper().readValue(possibleOutcomeDefinitionJson, object : TypeReference<Array<String>>() {})

    val result = processor.insertPossibleOutcomes(possibleOutcomeDefinitions, catalogue)

    assertThat(result.count()).isEqualTo(3)
    verify(possibleOutcomeRepository, times(1)).saveAll(anyList())
  }

  @Test
  fun `Providing Json to be extracted as a Risk Consideration Definition object to be stored into the database`() {
    val riskConsiderationDefinitionJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/RiskConsiderationDefinition.json")

    val riskConsiderationDefinition =
      ObjectMapper().readValue(riskConsiderationDefinitionJson, object : TypeReference<RiskConsiderationDefinition>() {})

    val result = processor.insertRiskConsideration(riskConsiderationDefinition, catalogue)

    assertThat(result.cnScoreGuide).isEqualTo("11.6 - Problem solving")
    verify(riskConsiderationRepository, times(1)).save(any())
  }

  @Test
  fun `Providing hasRoshLevel method with valid value`() {
    val result = processor.hasRoshLevel("HIGH")
    assertThat(result).isEqualTo(RoshLevel.HIGH)
  }

  @Test
  fun `Providing hasRoshLevel method with invalid value`() {
    val result = processor.hasRoshLevel("NOT_HIGH")
    assertThat(result).isEqualTo(null)
  }

  @Test
  fun `Providing Json to be extracted as a Special Educational Need object to be stored into the database`() {
    val specialEducationalNeedJson =
      InterventionLoadFileReaderHelper.getResource("classpath:db/interventions/SpecialEducationalNeedDefinition.json")

    val specialEducationalNeedDefinition =
      ObjectMapper().readValue(specialEducationalNeedJson, object : TypeReference<SpecialEducationalNeedDefinition>() {})

    val result = processor.insertSpecialEducationalNeed(specialEducationalNeedDefinition, catalogue)

    assertThat(result.literacyLevelGuide).isEqualTo("Able to read and understand simple sentences")
    verify(specialEducationalNeedRepository, times(1)).save(any())
  }
}
