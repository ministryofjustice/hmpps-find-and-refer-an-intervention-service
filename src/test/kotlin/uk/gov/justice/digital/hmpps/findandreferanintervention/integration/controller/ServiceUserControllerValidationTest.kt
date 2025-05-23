package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.TelemetryClientConfig
import uk.gov.justice.digital.hmpps.findandreferanintervention.controller.ServiceUserController
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ServiceUserService

@WebMvcTest(ServiceUserController::class)
@ActiveProfiles("test")
@Import(TelemetryClientConfig::class)
@WithMockUser(roles = ["FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"])
class ServiceUserControllerValidationTest(@Autowired private val mockMvc: MockMvc) {

  @MockitoBean
  private lateinit var serviceUserService: ServiceUserService

  @ParameterizedTest
  @ValueSource(strings = ["1234567", "A1234A", "X718255X"])
  fun `should return 400 when invalid crn is provided`(identifier: String) {
    val response = mockMvc.get("/service-user/{identifier}", identifier) {
    }.andReturn().response

    assertThat(response.status).isEqualTo(400)
    assertThat(response.contentAsString).contains("Invalid code format. Expected format for CRN: X718255 or PrisonNumber: A1234AA")
  }
}
