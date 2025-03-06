package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.toDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse
import java.util.UUID

class GetInterventionsDetailsTest : IntegrationTestBase() {
  @Autowired
  private lateinit var interventionRepository: InterventionCatalogueRepository

  @Test
  fun `getInterventionsDetails for ACP Building Better Relationships return 200 OK and InterventionDetailsDto`() {
    val interventionId = UUID.fromString("4902a268-9907-4070-ba48-7c2870a3b77e")
    val acpBbr =
      interventionRepository.findInterventionCatalogueById(interventionId)!!.toDetailsDto()

    webTestClient.get()
      .uri { it.path("/interventions/details/$interventionId").build() }
      .headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR")))
      .exchange()
      .expectStatus()
      .isOk
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody<InterventionDetailsDto>()
      .isEqualTo(acpBbr)
  }

  @Test
  fun `getInterventionsDetails for CRS Dependency & Recovery return 200 OK and InterventionDetailsDto`() {
    val interventionId = UUID.fromString("dd746a25-09b0-4d86-96ab-79e3539593a2")
    val crsDependenceAndRecovery =
      interventionRepository.findInterventionCatalogueById(interventionId)!!.toDetailsDto()

    webTestClient.get()
      .uri { it.path("/interventions/details/$interventionId").build() }
      .headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR")))
      .exchange()
      .expectStatus()
      .isOk
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody<InterventionDetailsDto>()
      .isEqualTo(crsDependenceAndRecovery)
  }

  @Test
  fun `getInterventionsDetails for non-existent ID return 404 NOT FOUND `() {
    val interventionId = "INVALID_UUID"

    webTestClient.get()
      .uri { it.path("/interventions/details/$interventionId").build() }
      .headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR")))
      .exchange()
      .expectStatus()
      .isBadRequest
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody<ErrorResponse>()
      .isEqualTo(
        ErrorResponse(
          status = BAD_REQUEST,
          userMessage = "Invalid value for parameter id",
          developerMessage = "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: $interventionId",
        ),
      )
  }

  @Test
  fun `getInterventionsDetails for non UUID return 400 BAD REQUEST`() {
    val interventionId = UUID.randomUUID()

    webTestClient.get()
      .uri { it.path("/interventions/details/$interventionId").build() }
      .headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR")))
      .exchange()
      .expectStatus()
      .isNotFound
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody<ErrorResponse>()
      .isEqualTo(
        ErrorResponse(
          status = NOT_FOUND,
          userMessage = "Intervention Not Found with ID: $interventionId",
          developerMessage = "404 NOT_FOUND \"Intervention Not Found with ID: $interventionId\"",
        ),
      )
  }

  @Test
  fun `getInterventionsDetails for non Authorised User return 403 Forbidden`() {
    val interventionId = UUID.randomUUID()

    webTestClient.get()
      .uri { it.path("/interventions/details/$interventionId").build() }
      .exchange()
      .expectStatus()
      .isUnauthorized
  }
}
