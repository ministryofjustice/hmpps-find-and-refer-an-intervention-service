package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import au.com.dius.pact.core.support.hasProperty
import com.microsoft.applicationinsights.TelemetryClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.TelemetryClientConfig
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.logToAppInsights
import uk.gov.justice.digital.hmpps.findandreferanintervention.controller.InterventionController
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.InterventionService
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.InterventionCatalogueFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.createDto

@Import(TelemetryClientConfig::class)
class InterventionControllerTest {
  private val telemetryClient = mock<TelemetryClient>()
  private val interventionService = mock<InterventionService>()
  private val interventionController =
    InterventionController(interventionService, telemetryClient)
  private val interventionCatalogueFactory: InterventionCatalogueFactory = InterventionCatalogueFactory()

  @BeforeEach
  fun beforeEach() {
    doNothing().`when`(telemetryClient).logToAppInsights(
      "InterventionsCatalogue Summary",
      mapOf("userMessage" to "User has hit interventions catalogue summary page"),
    )
  }

  @AfterEach
  fun afterEach() {
    reset(telemetryClient)
  }

  private fun verifyAppInsights() = verify(telemetryClient, times(1)).logToAppInsights(
    "InterventionsCatalogue Summary",
    mapOf("userMessage" to "User has hit interventions catalogue summary page"),
  )

  @Test
  fun `getInterventionsCatalogueByCriteria with no criteria when present return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val catalogue = interventionCatalogueFactory.createDto()
    whenever(
      interventionService.getInterventionsCatalogueByCriteria(
        pageable,
        null,
        SettingType.CUSTODY,
        null,
        null,
        null,
      ),
    ).thenReturn(PageImpl(listOf(catalogue)))
    val response = interventionController.getInterventionsCatalogue(pageable, null, null, null, null, SettingType.CUSTODY)

    verifyAppInsights()
    assertThat(response).isNotNull
    assertThat(response.content).isNotEmpty

    response.content.forEach { item ->
      assertThat(item).hasProperty("id")
      assertThat(item.hasProperty("criminogenicNeeds"))
      assertThat(item.hasProperty("title"))
      assertThat(item.hasProperty("description"))
      assertThat(item.hasProperty("deliveryFormat"))
      assertThat(item.hasProperty("interventionType"))
      assertThat(item.hasProperty("setting"))
      assertThat(item.hasProperty("allowsMales"))
      assertThat(item.hasProperty("allowsFemales"))
      assertThat(item.hasProperty("riskCriteria"))
      assertThat(item.hasProperty("attendanceType"))
      assertThat(item.hasProperty("suitableForPeopleWithLearningDifficulties"))
      assertThat(item.hasProperty("equivalentNonLdcProgramme"))
    }

    assertThat(response.content[0].title).isEqualTo("Finance, Benefit & Debt")
  }

  @Test
  fun `getInterventionsCatalogueByCriteria with no criteria when empty return a empty list of interventions`() {
    val pageable = PageRequest.of(0, 10)
    whenever(
      interventionService.getInterventionsCatalogueByCriteria(
        pageable,
        null,
        SettingType.CUSTODY,
        null,
        null,
        null,
      ),
    ).thenReturn(PageImpl(listOf()))
    val response = interventionController.getInterventionsCatalogue(pageable, null, null, null, null, SettingType.CUSTODY)

    verifyAppInsights()
    assertThat(response).isNotNull
    assertThat(response.content).isEmpty()
  }

  @Test
  fun `getInterventionsCatalogueByInterventionType when present return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val interventionTypes = listOf(InterventionType.ACP)
    val acpIntervention = interventionCatalogueFactory.createDto()
    whenever(
      interventionService.getInterventionsCatalogueByCriteria(
        pageable,
        interventionTypes,
        SettingType.CUSTODY,
        null,
        null,
        null,
      ),
    ).thenReturn(PageImpl(listOf(acpIntervention)))
    val response =
      interventionController.getInterventionsCatalogue(
        pageable,
        null,
        null,
        interventionTypes,
        null,
        SettingType.CUSTODY,
      )

    verifyAppInsights()
    assertThat(response).isNotNull
    assertThat(response.content).isNotEmpty
    response.content.forEach { item ->
      assertThat(item).hasProperty("id")
      assertThat(item.hasProperty("criminogenicNeeds"))
      assertThat(item.hasProperty("title"))
      assertThat(item.hasProperty("description"))
      assertThat(item.hasProperty("deliveryFormat"))
      assertThat(item.hasProperty("interventionType"))
      assertThat(item.hasProperty("setting"))
      assertThat(item.hasProperty("allowsMales"))
      assertThat(item.hasProperty("allowsFemales"))
      assertThat(item.hasProperty("riskCriteria"))
      assertThat(item.hasProperty("attendanceType"))
      assertThat(item.hasProperty("suitableForPeopleWithLearningDifficulties"))
      assertThat(item.hasProperty("equivalentNonLdcProgramme"))
    }
    assertThat(response.content[0].title).isEqualTo("Finance, Benefit & Debt")
  }

  @Test
  fun `getInterventionsCatalogueByInterventionType when searching by multiple types and they are present return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val interventionTypes = listOf(InterventionType.ACP, InterventionType.CRS)
    val acpIntervention = interventionCatalogueFactory.createDto(interventionType = InterventionType.ACP)
    val crsIntervention = interventionCatalogueFactory.createDto(interventionType = InterventionType.CRS)
    whenever(
      interventionService.getInterventionsCatalogueByCriteria(
        pageable,
        interventionTypes,
        SettingType.CUSTODY,
        null,
        null,
        null,
      ),
    )
      .thenReturn(PageImpl(listOf(acpIntervention, crsIntervention)))
    val response =
      interventionController.getInterventionsCatalogue(
        pageable,
        null,
        null,
        interventionTypes,
        null,
        SettingType.CUSTODY,
      )

    verifyAppInsights()
    assertThat(response).isNotNull
    assertThat(response.content).isNotEmpty
    response.content.forEach { item ->
      assertThat(item).hasProperty("id")
      assertThat(item.hasProperty("criminogenicNeeds"))
      assertThat(item.hasProperty("title"))
      assertThat(item.hasProperty("description"))
      assertThat(item.hasProperty("deliveryFormat"))
      assertThat(item.hasProperty("interventionType"))
      assertThat(item.hasProperty("setting"))
      assertThat(item.hasProperty("allowsMales"))
      assertThat(item.hasProperty("allowsFemales"))
      assertThat(item.hasProperty("riskCriteria"))
      assertThat(item.hasProperty("attendanceType"))
      assertThat(item.hasProperty("suitableForPeopleWithLearningDifficulties"))
      assertThat(item.hasProperty("equivalentNonLdcProgramme"))
    }
    assertThat(response.totalElements).isEqualTo(2)
    assertThat(response.content[0].title).isEqualTo("Finance, Benefit & Debt")
  }

  @Test
  fun `getInterventionsCatalogueByInterventionType when empty return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val interventionTypes = listOf(InterventionType.TOOLKITS)
    whenever(
      interventionService.getInterventionsCatalogueByCriteria(
        pageable,
        interventionTypes,
        SettingType.CUSTODY,
        null,
        null,
        null,
      ),
    )
      .thenReturn(PageImpl(listOf()))
    val response =

      interventionController.getInterventionsCatalogue(
        pageable,
        null,
        null,
        interventionTypes,
        null,
        SettingType.CUSTODY,
      )

    verifyAppInsights()
    assertThat(response).isNotNull
    assertThat(response.content).isEmpty()
  }
}
