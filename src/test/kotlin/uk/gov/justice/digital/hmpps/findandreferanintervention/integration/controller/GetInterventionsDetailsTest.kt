package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.jdbc.datasource.init.ScriptUtils
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeErrorResponse
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeRequestAndExpectJsonResponse
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeRequestAndExpectStatus
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse
import java.util.UUID
import javax.sql.DataSource

class GetInterventionsDetailsTest : IntegrationTestBase() {
  @Autowired
  private lateinit var interventionRepository: InterventionCatalogueRepository

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
  fun `getInterventionsDetails for ACP Healthy Identity Intervention return 200 OK and InterventionDetailsDto`() {
    val interventionId = UUID.fromString("7ce8b4ef-1429-4fc9-a7fe-706aab4dde78")
    val acpBbr =
      interventionRepository.findInterventionCatalogueById(interventionId)!!.toDetailsDto()

    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/details/$interventionId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
      expectedStatus = HttpStatus.OK,
      responseType = InterventionDetailsDto::class.java,
      expectedResponse = acpBbr,
    )
  }

  @Test
  fun `getInterventionsDetails for CRS Dependency & Recovery return 200 OK and InterventionDetailsDto`() {
    val interventionId = UUID.fromString("c5d53fbd-b7e3-40bd-9096-6720a01a53bf")
    val crsDependenceAndRecovery =
      interventionRepository.findInterventionCatalogueById(interventionId)!!.toDetailsDto()

    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/details/$interventionId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
      expectedStatus = HttpStatus.OK,
      responseType = InterventionDetailsDto::class.java,
      expectedResponse = crsDependenceAndRecovery,
    )
  }

  @Test
  fun `getInterventionsDetails for non-existent ID return 404 NOT FOUND `() {
    val interventionId = UUID.randomUUID()
    val expectedErrorResponse = makeErrorResponse(
      status = HttpStatus.NOT_FOUND,
      userMessage = "Intervention Not Found with ID: $interventionId",
      developerMessage = "404 NOT_FOUND \"Intervention Not Found with ID: $interventionId\"",
    )

    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/details/$interventionId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
      expectedStatus = HttpStatus.NOT_FOUND,
      responseType = ErrorResponse::class.java,
      expectedResponse = expectedErrorResponse,
    )
  }

  @Test
  fun `getInterventionsDetails for non UUID return 400 BAD REQUEST`() {
    val interventionId = " INVALID_UUID"
    val expectedErrorResponse = makeErrorResponse(
      status = HttpStatus.BAD_REQUEST,
      userMessage = "Invalid value for parameter id",
      developerMessage = "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: INVALID_UUID",
    )
    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/details/$interventionId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
      expectedStatus = HttpStatus.BAD_REQUEST,
      responseType = ErrorResponse::class.java,
      expectedResponse = expectedErrorResponse,
    )
  }

  @Test
  fun `getInterventionsDetails for non Authorised User return 401 Unauthorized`() {
    val interventionId = UUID.randomUUID()

    makeRequestAndExpectStatus(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/interventions/details/$interventionId").build() },
      requestCustomizer = { },
      expectedStatus = HttpStatus.UNAUTHORIZED,
    )
  }
}
