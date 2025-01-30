package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

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
  fun `getInterventionsCatalogue when present return a paged result of interventions`() {
    val pageable = PageRequest.of(0, 10)
    val catalogue = InterventionCatalogueDto(
      id = UUID.randomUUID(),
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
    whenever(interventionCatalogueService.getInterventionsCatalogue(pageable)).thenReturn(PageImpl(listOf(catalogue)))
    val response = interventionCatalogueController.getInterventionsCatalogue(pageable)

    verify(telemetryClient).trackEvent(
      "InterventionsCatalogue Summary",
      mapOf("userMessage" to "User has hit interventions catalogue summary page"),
      null,
    )

    assertThat(response).isNotNull
    assertThat(response.content).isNotEmpty
  }
}
