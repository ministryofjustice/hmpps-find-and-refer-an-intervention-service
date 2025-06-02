package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import com.microsoft.applicationinsights.TelemetryClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.logToAppInsights
import uk.gov.justice.digital.hmpps.findandreferanintervention.controller.ServiceUserController
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.ServiceUserDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ServiceUserService
import java.time.LocalDate

internal class ServiceUserControllerTest {
  private val service = mock<ServiceUserService>()
  private val telemetryClient = mock<TelemetryClient>()
  private val controller = ServiceUserController(service, telemetryClient)

  private fun jwtAuthToken(): JwtAuthenticationToken {
    val jwt = Jwt.withTokenValue("token")
      .header("alg", "none")
      .claim("user_name", "testuser")
      .build()
    return JwtAuthenticationToken(jwt)
  }

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
    whenever(service.checkIfAuthenticatedDeliusUserHasAccessToServiceUser(any(), any())).thenReturn(true)
    whenever(service.getServiceUserByIdentifier(any())).thenReturn(testServiceUser)

    val response = controller.serviceUserByCrnOrPrisonerNumber(identifier = identifier, jwtAuthToken())

    verify(telemetryClient).logToAppInsights(
      "retrieve service user details",
      mapOf("userMessage" to "User has hit service user details endpoint", "identifier" to identifier),
    )

    assertThat(response).isNotNull
    assertThat(response?.name).isEqualTo("John")
    assertThat(response?.crn).isEqualTo("X718255")
    assertThat(response?.setting).isEqualTo("Community")
  }

  @Test
  fun `throws AccessDeniedException when user does not have access`() {
    whenever(service.checkIfAuthenticatedDeliusUserHasAccessToServiceUser(any(), any())).thenReturn(false)
    val exception = assertThrows<AccessDeniedException> {
      controller.serviceUserByCrnOrPrisonerNumber("X718255", jwtAuthToken())
    }
    assertThat(exception.message).isEqualTo("You are not authorized to view this person's details. Either contact your administrator or enter a different CRN or Prison Number")
  }
}
