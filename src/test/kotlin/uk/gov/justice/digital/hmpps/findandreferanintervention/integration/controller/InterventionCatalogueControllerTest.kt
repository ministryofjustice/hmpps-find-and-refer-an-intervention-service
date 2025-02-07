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
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.InterventionCatalogueService
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.InterventionCatalogueFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.createDto

internal class InterventionCatalogueControllerTest {
  private val telemetryClient = mock<TelemetryClient>()
  private val interventionCatalogueService = mock<InterventionCatalogueService>()
  private val interventionCatalogueController =
    InterventionCatalogueController(interventionCatalogueService, telemetryClient)
  private val interventionCatalogueFactory: InterventionCatalogueFactory = InterventionCatalogueFactory()

  @Test
  fun `getInterventionsCatalogueByCriteria with no criteria when present return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val catalogue = interventionCatalogueFactory.createDto()
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
      assertThat(item.hasProperty("minAge"))
      assertThat(item.hasProperty("maxAge"))
      assertThat(item.hasProperty("riskCriteria"))
      assertThat(item.hasProperty("attendanceType"))
    }

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
    val acpIntervention = interventionCatalogueFactory.createDto()
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
      interventionCatalogueController.getInterventionsCatalogue(pageable, null, null, interventionTypes, null)

    verify(telemetryClient)
      .trackEvent(
        "InterventionsCatalogue Summary",
        mapOf("userMessage" to "User has hit interventions catalogue summary page"),
        null,
      )

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
      assertThat(item.hasProperty("minAge"))
      assertThat(item.hasProperty("maxAge"))
      assertThat(item.hasProperty("riskCriteria"))
      assertThat(item.hasProperty("attendanceType"))
    }
    assertThat(response.content[0].title).isEqualTo("Test Title")
  }

  @Test
  fun `getInterventionsCatalogueByInterventionType when searching by multiple types and they are present return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val interventionTypes = listOf(InterventionType.ACP, InterventionType.CRS)
    val acpIntervention = interventionCatalogueFactory.createDto(interventionType = InterventionType.ACP)
    val crsIntervention = interventionCatalogueFactory.createDto(interventionType = InterventionType.CRS)
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
      interventionCatalogueController.getInterventionsCatalogue(pageable, null, null, interventionTypes, null)

    verify(telemetryClient)
      .trackEvent(
        "InterventionsCatalogue Summary",
        mapOf("userMessage" to "User has hit interventions catalogue summary page"),
        null,
      )

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
      assertThat(item.hasProperty("minAge"))
      assertThat(item.hasProperty("maxAge"))
      assertThat(item.hasProperty("riskCriteria"))
      assertThat(item.hasProperty("attendanceType"))
    }
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
      interventionCatalogueController.getInterventionsCatalogue(pageable, null, null, interventionTypes, null)

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
