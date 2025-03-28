package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.jdbc.datasource.init.ScriptUtils
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.CrsInterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toCrsDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeErrorResponse
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeRequestAndExpectJsonResponse
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeRequestAndExpectStatus
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse
import java.util.UUID
import javax.sql.DataSource

class GetCrsInterventionDetailsTest : IntegrationTestBase() {

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
  fun `getCrsInterventionDetails return 200 when intervention exists`() {
    val interventionId = UUID.fromString("c5d53fbd-b7e3-40bd-9096-6720a01a53bf")
    val pduId = "redcar-cleveland-and-middlesbrough"
    val interventionCatalogue = interventionRepository.findInterventionCatalogueById(interventionId)
    val crsInterventionDetailsDto =
      interventionCatalogue!!.toCrsDetailsDto(interventionCatalogue.interventions.first())

    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/intervention/$interventionId/pdu/$pduId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
      expectedStatus = HttpStatus.OK,
      responseType = CrsInterventionDetailsDto::class.java,
      expectedResponse = crsInterventionDetailsDto,
    )
  }

  @Test
  fun `getCrsInterventionDetails return 404 when intervention is not found`() {
    val interventionId = UUID.randomUUID()
    val pduId = "redcar-cleveland-and-middlesbrough"
    val expectedErrorResponse = makeErrorResponse(
      status = HttpStatus.NOT_FOUND,
      userMessage = "Intervention Not Found with ID: $interventionId and PduId $pduId",
      developerMessage = "404 NOT_FOUND \"Intervention Not Found with ID: $interventionId and PduId $pduId\"",
    )
    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/intervention/$interventionId/pdu/$pduId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
      expectedStatus = HttpStatus.NOT_FOUND,
      responseType = ErrorResponse::class.java,
      expectedResponse = expectedErrorResponse,
    )
  }

  @Test
  fun `getCrsInterventionDetails return 404 when pdu is not found`() {
    val interventionId = UUID.fromString("3ccb511b-89b2-42f7-803b-304f54d85a24")
    val pduId = "INVALID_PDU"
    val expectedErrorResponse = makeErrorResponse(
      status = HttpStatus.NOT_FOUND,
      userMessage = "Intervention Not Found with ID: $interventionId and PduId $pduId",
      developerMessage = "404 NOT_FOUND \"Intervention Not Found with ID: $interventionId and PduId $pduId\"",
    )

    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/intervention/$interventionId/pdu/$pduId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR"))) },
      expectedStatus = HttpStatus.NOT_FOUND,
      responseType = ErrorResponse::class.java,
      expectedResponse = expectedErrorResponse,
    )
  }

  @Test
  fun `getCrsInterventionsDetails for non Authorised User return 403 Forbidden`() {
    val interventionId = UUID.randomUUID()
    val pduId = "PDU_ID"

    webTestClient.get()
      .uri { it.path("/intervention/$interventionId/pdu/$pduId").build() }
      .exchange()
      .expectStatus()
      .isUnauthorized

    makeRequestAndExpectStatus(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/intervention/$interventionId/pdu/$pduId").build() },
      requestCustomizer = { },
      expectedStatus = HttpStatus.UNAUTHORIZED,
    )
  }
}
