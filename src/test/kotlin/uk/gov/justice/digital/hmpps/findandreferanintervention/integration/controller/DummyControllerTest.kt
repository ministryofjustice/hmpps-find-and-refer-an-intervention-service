package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import com.microsoft.applicationinsights.TelemetryClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.findandreferanintervention.controller.DummyController
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Dummy
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DummyRepository
import java.time.LocalDateTime

internal class DummyControllerTest {
  private val dummyRepository = mock<DummyRepository>()
  private val telemetryClient = org.mockito.kotlin.mock<TelemetryClient>()
  private val dummyController = DummyController(dummyRepository, telemetryClient)

  @Test
  fun `getDummyRecordById returns Dummy when present`() {
    val testDummyRecord = Dummy(1, "dummy text 1", LocalDateTime.parse("2024-12-12T12:00"))
    whenever(dummyRepository.findByDummyId(any())).thenReturn(testDummyRecord)
    val response = dummyController.getDummy(1)

    verify(telemetryClient).trackEvent("LoggingToAppInsights", mapOf("userMessage" to "User has hit the dummy endpoint", "dummyId" to "1"), null)

    assertThat(response).isNotNull
    assertThat(response?.dummyDescription).isEqualTo("dummy text 1")
    assertThat(response?.dummyId).isEqualTo(1)
  }

  @Test
  fun `getDummyRecordById returns null when not present`() {
    whenever(dummyRepository.findByDummyId(any())).thenReturn(null)
    val response = dummyController.getDummy(50)

    verify(telemetryClient).trackEvent("LoggingToAppInsights", mapOf("userMessage" to "User has hit the dummy endpoint", "dummyId" to "50"), null)

    assertThat(response).isNull()
  }
}
