package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import au.com.dius.pact.core.support.hasProperty
import com.microsoft.applicationinsights.TelemetryClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import uk.gov.justice.digital.hmpps.findandreferanintervention.controller.InterventionCatalogueController
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.InterventionCatalogueService
import java.util.UUID

internal class InterventionCatalogueControllerTest {
  private val telemetryClient = mock<TelemetryClient>()
  private val interventionCatalogueService = mock<InterventionCatalogueService>()
  private val interventionCatalogueController =
    InterventionCatalogueController(interventionCatalogueService, telemetryClient)

  @Test
  fun `getInterventionsCatalogueByCriteria with no criteria when present return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val catalogue =
      InterventionCatalogueDto(
        id = UUID.randomUUID(),
        criminogenicNeeds = listOf("NEED_1", "NEED_2"),
        title = "Test Title",
        description = "Test Description",
        deliveryFormat = listOf("In Person"),
        interventionType = InterventionType.ACP,
        setting = listOf(SettingType.COMMUNITY),
        allowsMales = true,
        allowsFemales = true,
        minAge = 18,
        maxAge = 30,
        riskCriteria = listOf("RISK_CRITERIA_1", "RISK_CRITERIA_2"),
        attendanceType = listOf("One-to-one"),
      )
    whenever(interventionCatalogueService.getInterventionsCatalogueByCriteria(pageable, null, null, null, null))
      .thenReturn(PageImpl(listOf(catalogue)))
    val response = interventionCatalogueController.getInterventionsCatalogue(pageable, null, null, null, null)

    verify(telemetryClient)
      .trackEvent(
        "InterventionsCatalogue Summary",
        mapOf("userMessage" to "User has hit interventions catalogue summary page"),
        null,
      )

    assertThat(response).isNotNull
    assertThat(response.content).isNotEmpty
    assertThat(
      response.content.all {
        it.hasProperty("id")
        it.hasProperty("title")
        it.hasProperty("description")
        it.hasProperty("deliveryFormat")
        it.hasProperty("interventionTyps")
        it.hasProperty("setting")
        it.hasProperty("allowsMales")
        it.hasProperty("allowsFemales")
        it.hasProperty("minAge")
        it.hasProperty("maxAge")
        it.hasProperty("riskCriteria")
        it.hasProperty("attendanceType")
      },
    )
    assertThat(response.content[0].title).isEqualTo("Test Title")
  }

  @Test
  fun `getInterventionsCatalogueByCriteria with no criteria when empty return a empty list of interventions`() {
    val pageable = PageRequest.of(0, 10)
    whenever(interventionCatalogueService.getInterventionsCatalogueByCriteria(pageable, null, null, null, null))
      .thenReturn(PageImpl(listOf()))
    val response = interventionCatalogueController.getInterventionsCatalogue(pageable, null, null, null, null)

    verify(telemetryClient)
      .trackEvent(
        "InterventionsCatalogue Summary",
        mapOf("userMessage" to "User has hit interventions catalogue summary page"),
        null,
      )

    assertThat(response).isNotNull
    assertThat(response.content).isEmpty()
  }

  @Test
  fun `getInterventionsCatalogueByInterventionType when present return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val interventionTypes = listOf(InterventionType.ACP)
    val acpIntervention =
      InterventionCatalogueDto(
        id = UUID.randomUUID(),
        criminogenicNeeds = listOf("NEED_1", "NEED_2"),
        title = "Test Title",
        description = "Test Description",
        deliveryFormat = listOf("In Person"),
        interventionType = InterventionType.ACP,
        setting = listOf(SettingType.COMMUNITY),
        allowsMales = true,
        allowsFemales = true,
        minAge = 18,
        maxAge = 30,
        riskCriteria = listOf("RISK_CRITERIA_1", "RISK_CRITERIA_2"),
        attendanceType = listOf("One-to-one"),
      )
    whenever(
      interventionCatalogueService.getInterventionsCatalogueByCriteria(
        pageable,
        interventionTypes,
        null,
        null,
        null,
      ),
    )
      .thenReturn(PageImpl(listOf(acpIntervention)))
    val response =
      interventionCatalogueController.getInterventionsCatalogue(pageable, interventionTypes, null, null, null)

    verify(telemetryClient)
      .trackEvent(
        "InterventionsCatalogue Summary",
        mapOf("userMessage" to "User has hit interventions catalogue summary page"),
        null,
      )

    assertThat(response).isNotNull
    assertThat(response.content).isNotEmpty
    assertThat(
      response.content.all {
        it.hasProperty("id")
        it.hasProperty("criminogenicNeeds")
        it.hasProperty("title")
        it.hasProperty("description")
        it.hasProperty("deliveryFormat")
        it.hasProperty("interventionType")
        it.hasProperty("setting")
        it.hasProperty("allowsMales")
        it.hasProperty("allowsFemales")
        it.hasProperty("minAge")
        it.hasProperty("maxAge")
        it.hasProperty("riskCriteria")
        it.hasProperty("attendanceType")
      },
    )
    assertThat(response.content[0].title).isEqualTo("Test Title")
  }

  @Test
  fun `getInterventionsCatalogueByInterventionType when searching by multiple types and they are present return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val interventionTypes = listOf(InterventionType.ACP, InterventionType.CRS)
    val acpIntervention =
      InterventionCatalogueDto(
        id = UUID.randomUUID(),
        criminogenicNeeds = listOf("NEED_1", "NEED_2"),
        title = "Test Title",
        description = "Test Description",
        deliveryFormat = listOf("In Person"),
        interventionType = InterventionType.ACP,
        setting = listOf(SettingType.COMMUNITY),
        allowsMales = true,
        allowsFemales = true,
        minAge = 18,
        maxAge = 30,
        riskCriteria = listOf("RISK_CRITERIA_1", "RISK_CRITERIA_2"),
        attendanceType = listOf("One-to-one"),
      )
    val crsIntervention =
      InterventionCatalogueDto(
        id = UUID.randomUUID(),
        criminogenicNeeds = listOf("NEED_1", "NEED_2"),
        title = "Test Title",
        description = "Test Description",
        deliveryFormat = listOf("In Person"),
        interventionType = InterventionType.CRS,
        setting = listOf(SettingType.COMMUNITY),
        allowsMales = true,
        allowsFemales = true,
        minAge = 18,
        maxAge = 30,
        riskCriteria = listOf("RISK_CRITERIA_1", "RISK_CRITERIA_2"),
        attendanceType = listOf("One-to-one"),
      )
    whenever(
      interventionCatalogueService.getInterventionsCatalogueByCriteria(
        pageable,
        interventionTypes,
        null,
        null,
        null,
      ),
    )
      .thenReturn(PageImpl(listOf(acpIntervention, crsIntervention)))
    val response =
      interventionCatalogueController.getInterventionsCatalogue(pageable, interventionTypes, null, null, null)

    verify(telemetryClient)
      .trackEvent(
        "InterventionsCatalogue Summary",
        mapOf("userMessage" to "User has hit interventions catalogue summary page"),
        null,
      )

    assertThat(response).isNotNull
    assertThat(response.content).isNotEmpty
    assertThat(
      response.content.all {
        it.hasProperty("id")
        it.hasProperty("criminogenicNeeds")
        it.hasProperty("title")
        it.hasProperty("description")
        it.hasProperty("deliveryFormat")
        it.hasProperty("interventionType")
        it.hasProperty("setting")
        it.hasProperty("allowsMales")
        it.hasProperty("allowsFemales")
        it.hasProperty("minAge")
        it.hasProperty("maxAge")
        it.hasProperty("riskCriteria")
        it.hasProperty("attendanceType")
      },
    )
    assertThat(response.totalElements).isEqualTo(2)
    assertThat(response.content[0].title).isEqualTo("Test Title")
  }

  @Test
  fun `getInterventionsCatalogueByInterventionType when empty return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val interventionTypes = listOf(InterventionType.ACP)
    whenever(
      interventionCatalogueService.getInterventionsCatalogueByCriteria(
        pageable,
        interventionTypes,
        null,
        null,
        null,
      ),
    )
      .thenReturn(PageImpl(listOf()))
    val response =
      interventionCatalogueController.getInterventionsCatalogue(pageable, interventionTypes, null, null, null)

    verify(telemetryClient)
      .trackEvent(
        "InterventionsCatalogue Summary",
        mapOf("userMessage" to "User has hit interventions catalogue summary page"),
        null,
      )

    assertThat(response).isNotNull
    assertThat(response.content).isEmpty()
  }
}
