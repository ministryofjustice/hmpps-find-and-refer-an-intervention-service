package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import com.microsoft.applicationinsights.TelemetryClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

  @Test
  fun `serviceUserByCrn returns ServiceUserDto when CRN is provided`() {
    val testServiceUser = ServiceUserDto(
      name = "John",
      crn = "X718255",
      dob = LocalDate.parse("1990-01-01"),
      gender = "Male",
      ethnicity = "White British",
      currentPdu = "East Sussex",
    )
    whenever(service.getServiceUserByCrn(any())).thenReturn(testServiceUser)

    val response = controller.serviceUserByCrn(crn = "X718255", prisonId = null)

    verify(telemetryClient).logToAppInsights(
      "retrieve service user details",
      mapOf("userMessage" to "User has hit service user details endpoint", "crn" to "X718255"),
    )

    assertThat(response).isNotNull
    assertThat(response.name).isEqualTo("John")
    assertThat(response.crn).isEqualTo("X718255")
  }

  @Test
  fun `serviceUserByCrn returns ServiceUserDto when prisonId is provided`() {
    val testServiceUser = ServiceUserDto(
      name = "Jane",
      crn = "X123456",
      dob = LocalDate.parse("1985-05-15"),
      gender = "Female",
      ethnicity = "British",
      currentPdu = "West Sussex",
    )
    whenever(service.getServiceUserByPrisonId(any())).thenReturn(testServiceUser)

    val response = controller.serviceUserByCrn(crn = null, prisonId = "A1234AA")

    verify(telemetryClient).logToAppInsights(
      "retrieve service user details",
      mapOf("userMessage" to "User has hit service user details endpoint", "prisonId" to "A1234AA"),
    )

    assertThat(response).isNotNull
    assertThat(response.name).isEqualTo("Jane")
    assertThat(response.crn).isEqualTo("X123456")
  }

  @Test
  fun `serviceUserByCrn throws IllegalArgumentException when neither crn nor prisonId is provided`() {
    val exception = assertThrows<IllegalArgumentException> {
      controller.serviceUserByCrn(crn = null, prisonId = null)
    }
    assertThat(exception.message).isEqualTo("Either crn or prisonId must be provided")
  }
}
