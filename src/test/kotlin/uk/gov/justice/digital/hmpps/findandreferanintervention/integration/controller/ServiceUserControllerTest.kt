package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import com.microsoft.applicationinsights.TelemetryClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.logToAppInsights
import uk.gov.justice.digital.hmpps.findandreferanintervention.controller.ServiceUserController
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.ServiceUserDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ServiceUserService
import java.time.LocalDate

internal class ServiceUserControllerTest {
  private val service = mock<ServiceUserService>()
  private val telemetryClient = mock<TelemetryClient>()
  private val controller = ServiceUserController(service, telemetryClient)

  @ParameterizedTest
  @ValueSource(strings = ["X718255", "A1234DE"])
  fun `serviceUserByCrnOrPrisonerNumber returns ServiceUserDto when CRN is provided`(identifier: String) {
    val testServiceUser = ServiceUserDto(
      name = "John",
      crn = "X718255",
      dob = LocalDate.parse("1990-01-01"),
      gender = "Male",
      ethnicity = "White British",
      currentPdu = "East Sussex",
      setting = "Community",
    )
    whenever(service.getServiceUserByIdentifier(any())).thenReturn(testServiceUser)

    val response = controller.serviceUserByCrnOrPrisonerNumber(identifier = identifier)

    verify(telemetryClient).logToAppInsights(
      "retrieve service user details",
      mapOf("userMessage" to "User has hit service user details endpoint", "identifier" to identifier),
    )

    assertThat(response).isNotNull
    assertThat(response?.name).isEqualTo("John")
    assertThat(response?.crn).isEqualTo("X718255")
    assertThat(response?.setting).isEqualTo("Community")
  }
}
