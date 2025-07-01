package uk.gov.justice.digital.hmpps.findandreferanintervention.integration

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.jdbc.datasource.init.ScriptUtils
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.ReferralDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.ReferralRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeErrorResponse
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeRequestAndExpectJsonResponse
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.makeRequestAndExpectStatus
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse
import java.util.UUID
import javax.sql.DataSource

class GetReferralDetailsTest : IntegrationTestBase() {
  @Autowired
  private lateinit var referralRepository: ReferralRepository

  @Autowired
  private lateinit var dataSource: DataSource

  @Autowired
  private lateinit var resourceLoader: ResourceLoader

  @BeforeEach
  fun beforeEach() {
    dataSource.connection.use {
      val r = resourceLoader.getResource("classpath:testData/setup.sql")
      ScriptUtils.executeSqlScript(it, r)
      val referrals = resourceLoader.getResource("classpath:testData/referrals.sql")
      ScriptUtils.executeSqlScript(it, referrals)
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
  fun `getReferral details for Licence condition created referral returns 200 and referralDetailsDto`() {
    val referralId = UUID.fromString("3e183d36-85c2-4a43-8ca5-ff6422b064cb")
    val referralDetailsDto = referralRepository.findReferralById(referralId)!!.toDto()

    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/referral/$referralId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_ACCREDITED_PROGRAMMES_MANAGE_AND_DELIVER_API__ACPMAD_UI_WR"))) },
      expectedStatus = HttpStatus.OK,
      responseType = ReferralDetailsDto::class.java,
      expectedResponse = referralDetailsDto,
    )
  }

  @Test
  fun `getReferral details for Requirement created referral returns 200 and referralDetailsDto`() {
    val referralId = UUID.fromString("a410c4bd-027a-436a-a3f4-4fa22039e314")
    val referralDetailsDto = referralRepository.findReferralById(referralId)!!.toDto()

    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/referral/$referralId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_ACCREDITED_PROGRAMMES_MANAGE_AND_DELIVER_API__ACPMAD_UI_WR"))) },
      expectedStatus = HttpStatus.OK,
      responseType = ReferralDetailsDto::class.java,
      expectedResponse = referralDetailsDto,
    )
  }

  @Test
  fun `getReferralDetails for non-existent ID return 404 NOT FOUND`() {
    val referralId = UUID.randomUUID()
    val expectedErrorResponse = makeErrorResponse(
      status = HttpStatus.NOT_FOUND,
      userMessage = "Referral Not Found with ID: $referralId",
      developerMessage = "404 NOT_FOUND \"Referral Not Found with ID: $referralId\"",
    )

    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/referral/$referralId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_ACCREDITED_PROGRAMMES_MANAGE_AND_DELIVER_API__ACPMAD_UI_WR"))) },
      expectedStatus = HttpStatus.NOT_FOUND,
      responseType = ErrorResponse::class.java,
      expectedResponse = expectedErrorResponse,
    )
  }

  @Test
  fun `getReferralDetails for non UUID return 400 BAD REQUEST`() {
    val referralId = " INVALID_UUID"
    val expectedErrorResponse = makeErrorResponse(
      status = HttpStatus.BAD_REQUEST,
      userMessage = "Invalid value for parameter referralId",
      developerMessage = "Method parameter 'referralId': Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: INVALID_UUID",
    )
    makeRequestAndExpectJsonResponse(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/referral/$referralId").build() },
      requestCustomizer = { headers(setAuthorisation(roles = listOf("ROLE_ACCREDITED_PROGRAMMES_MANAGE_AND_DELIVER_API__ACPMAD_UI_WR"))) },
      expectedStatus = HttpStatus.BAD_REQUEST,
      responseType = ErrorResponse::class.java,
      expectedResponse = expectedErrorResponse,
    )
  }

  @Test
  fun `getReferralDetails for non Authorised User return 401 Unauthorized`() {
    val referralId = UUID.randomUUID()

    makeRequestAndExpectStatus(
      testClient = webTestClient,
      httpMethod = HttpMethod.GET,
      uri = { it.path("/referral/$referralId").build() },
      requestCustomizer = { },
      expectedStatus = HttpStatus.UNAUTHORIZED,
    )
  }
}
