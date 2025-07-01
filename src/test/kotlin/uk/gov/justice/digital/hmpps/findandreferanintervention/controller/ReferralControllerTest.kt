package uk.gov.justice.digital.hmpps.findandreferanintervention.controller

import com.microsoft.applicationinsights.TelemetryClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.context.annotation.Import
import org.springframework.web.server.ResponseStatusException
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.TelemetryClientConfig
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.logToAppInsights
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ReferralService
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.ReferralFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.create
import java.util.UUID

@Import(TelemetryClientConfig::class)
class ReferralControllerTest {

  private val telemetryClient = mock<TelemetryClient>()
  private val referralService = mock<ReferralService>()
  private val referralController: ReferralController = ReferralController(referralService, telemetryClient)
  private val referralFactory: ReferralFactory = ReferralFactory()

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

  @Test
  fun `get referral with existing id returns a referralDetailsDto`() {
    val referral = referralFactory.create()
    whenever(referralService.getReferralDetailsById(any())).thenReturn(referral.toDto())

    val response = referralController.getReferralDetails(referral.id)
    verify(telemetryClient, times(1)).logToAppInsights(
      "Received request for referral details",
      mapOf("referralId" to referral.id.toString()),
    )
    assertThat(response).isNotNull
    assertThat(response!!).isEqualTo(referral.toDto())
  }

  @Test
  fun `get referral with non existent id returns Not Found exception`() {
    val randomUuid = UUID.randomUUID()
    whenever(referralService.getReferralDetailsById(any())).thenReturn(null)
    val exception = assertThrows<ResponseStatusException> {
      referralController.getReferralDetails(randomUuid)
    }
    assertThat(exception.message).isEqualTo("404 NOT_FOUND \"Referral Not Found with ID: $randomUuid\"")
    verify(telemetryClient, times(1)).logToAppInsights(
      "Received request for referral details",
      mapOf("referralId" to randomUuid.toString()),
    )
  }
}
