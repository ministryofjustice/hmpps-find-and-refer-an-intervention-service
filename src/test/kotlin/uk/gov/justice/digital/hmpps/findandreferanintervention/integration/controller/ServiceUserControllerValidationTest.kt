package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.TelemetryClientConfig
import uk.gov.justice.digital.hmpps.findandreferanintervention.controller.ServiceUserController
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ServiceUserService
import java.util.Collections

@WebMvcTest(ServiceUserController::class)
@ActiveProfiles("test")
@Import(TelemetryClientConfig::class)
class ServiceUserControllerValidationTest(@Autowired private val mockMvc: MockMvc) {

  @MockitoBean
  private lateinit var serviceUserService: ServiceUserService

  @ParameterizedTest
  @ValueSource(strings = ["1234567", "A1234A", "X718255X"])
  fun `should return 400 when invalid crn is provided`(identifier: String) {
    val jwt = Jwt.withTokenValue("token")
      .header("alg", "none")
      .claim("user_name", "testuser")
      .build()
    val jwtAuth = JwtAuthenticationToken(
      jwt,
      Collections.singletonList(SimpleGrantedAuthority("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR")),
    ) // Add appropriate roles

    whenever(serviceUserService.checkIfAuthenticatedDeliusUserHasAccessToServiceUser(any(), any())).thenReturn(true)

    val response = mockMvc.get("/service-user/{identifier}", identifier) {
      with(authentication(jwtAuth))
    }.andReturn().response

    assertThat(response.status).isEqualTo(400)
    assertThat(response.contentAsString).contains("Invalid code format. Expected format for CRN: X718255 or PrisonNumber: A1234AA")
  }

  @Test
  fun `should return 403 when limited access offender check says the user is restricted for this crn or prisoner number`() {
    val jwt = Jwt.withTokenValue("token")
      .header("alg", "none")
      .claim("user_name", "testuser")
      .build()
    val jwtAuth = JwtAuthenticationToken(
      jwt,
      Collections.singletonList(SimpleGrantedAuthority("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR")),
    ) // Add appropriate roles

    whenever(serviceUserService.checkIfAuthenticatedDeliusUserHasAccessToServiceUser(any(), any())).thenReturn(false)

    val response = mockMvc.get("/service-user/{identifier}", "X718255") {
      with(authentication(jwtAuth))
    }.andReturn().response

    assertThat(response.status).isEqualTo(403)
    assertThat(response.contentAsString).contains("You are not authorized to view this person's details. Either contact your administrator or enter a different CRN or Prison Number")
  }
}
