package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.jdbc.datasource.init.ScriptUtils
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeErrorResponse
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeRequest
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeRequestAndExpectJsonResponse
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeRequestAndExpectStatus
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse
import javax.sql.DataSource

class GetInterventionsCatalogueTest : IntegrationTestBase() {
  @Autowired
  private lateinit var dataSource: DataSource

  @Autowired
  private lateinit var resourceLoader: ResourceLoader

  @BeforeEach
  fun beforeEach() {
    dataSource.connection.use {
      val r = resourceLoader.getResource("classpath:testData/setup.sql")
      ScriptUtils.executeSqlScript(it, r)
    }
  }

  @AfterEach
  fun afterEach() {
    dataSource.connection.use {
      val r = resourceLoader.getResource("classpath:testData/teardown.sql")
      ScriptUtils.executeSqlScript(it, r)
    }
  }

  @Test
  fun `getInterventionsCatalogue for COMMUNITY Interventions return 200 and a paged list of Interventions`() {
    makeRequest(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/community").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
    )
      .expectStatus()
      .isEqualTo(HttpStatus.OK)
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.content").exists()
      .jsonPath("$.totalElements").isEqualTo(11)
  }

  @Test
  fun `getInterventionsCatalogue for CUSTODY Interventions return 200 and a paged list of Interventions`() {
    makeRequest(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/custody").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
    )
      .expectStatus()
      .isEqualTo(HttpStatus.OK)
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.content").exists()
      .jsonPath("$.totalElements").isEqualTo(4)
  }

  @Test
  fun `getInterventionsCatalogue for COMMUNITY and programmeName = 'Dependency and Recovery' return 200 and a paged list of Interventions`() {
    makeRequest(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/COMMUNITY").queryParam("programmeName", "Dependency and Recovery").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
    )
      .expectStatus()
      .isEqualTo(HttpStatus.OK)
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.content").exists()
      .jsonPath("$.totalElements").isEqualTo(1)
  }

  @Test
  fun `getInterventionsCatalogue invalid intervention type returns 400 BAD REQUEST`() {
    val expectedErrorResponse = makeErrorResponse(
      status = HttpStatus.BAD_REQUEST,
      userMessage = "Invalid value for parameter interventionTypes",
      developerMessage = "Method parameter 'interventionType': Failed to convert value of type 'java.lang.String' to required type 'java.util.List'; Failed to convert from type [java.lang.String] to type [@org.springframework.web.bind.annotation.RequestParam uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType] for value [INVALID_TYPE]",
    )
    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/CUSTODY").queryParam("interventionType", "INVALID_TYPE").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
      expectedStatus = HttpStatus.BAD_REQUEST,
      responseType = ErrorResponse::class.java,
      expectedResponse = expectedErrorResponse,
    )
  }

  @Test
  fun `getInterventionsCatalogue invalid boolean returns 400 BAD REQUEST`() {
    val expectedErrorResponse = makeErrorResponse(
      status = HttpStatus.BAD_REQUEST,
      userMessage = "Invalid value for parameter allowsMales",
      developerMessage = "Method parameter 'allowsMales': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Boolean'; Invalid boolean value [INVALID_TYPE]",
    )
    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/CUSTODY").queryParam("allowsMales", "INVALID_TYPE").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
      expectedStatus = HttpStatus.BAD_REQUEST,
      responseType = ErrorResponse::class.java,
      expectedResponse = expectedErrorResponse,
    )
  }

  @Test
  fun `getInterventionsCatalogue for non Authorised User return 401 Unauthorized`() {
    makeRequestAndExpectStatus(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/CUSTODY").build() },
      requestCustomizer = { },
      expectedStatus = HttpStatus.UNAUTHORIZED,
    )
  }
}
