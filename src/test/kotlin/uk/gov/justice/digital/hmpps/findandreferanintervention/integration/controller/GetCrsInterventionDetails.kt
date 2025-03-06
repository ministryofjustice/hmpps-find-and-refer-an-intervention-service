package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.expectBody
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.CrsInterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toCrsDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PduRefRepository
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse
import java.util.UUID

class GetCrsInterventionDetails : IntegrationTestBase() {
  @Autowired
  private lateinit var interventionRepository: InterventionCatalogueRepository

  @Autowired
  private lateinit var pduRefRepository: PduRefRepository

  @Test
  fun `getCrsInterventionDetails return 200 when intervention exists`() {
    val interventionId = UUID.fromString("ce0bf924-d5eb-498f-9376-8a01a07510f5")
    val pduId = "redcar-cleveland-and-middlesbrough"
    val pduRef = pduRefRepository.findPduRefById(pduId)!!
    val pduDetails = interventionRepository.findInterventionCatalogueById(interventionId)!!.toCrsDetailsDto(pduRef)

    webTestClient.get()
      .uri { it.path("/intervention/$interventionId/pdu/$pduId").build() }
      .headers(setAuthorisation(roles = listOf(interventionClientRole)))
      .exchange()
      .expectStatus()
      .isOk
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody<CrsInterventionDetailsDto>()
      .isEqualTo(pduDetails)
  }

  @Test
  fun `getCrsInterventionDetails return 404 when intervention is not found`() {
    val interventionId = UUID.randomUUID()
    val pduId = "redcar-cleveland-and-middlesbrough"

    webTestClient.get()
      .uri { it.path("/intervention/$interventionId/pdu/$pduId").build() }
      .headers(setAuthorisation(roles = listOf(interventionClientRole)))
      .exchange()
      .expectStatus()
      .isNotFound
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody<ErrorResponse>()
      .isEqualTo(
        ErrorResponse(
          status = HttpStatus.NOT_FOUND,
          userMessage = "Intervention Not Found with ID: $interventionId and PduId $pduId",
          developerMessage = "404 NOT_FOUND \"Intervention Not Found with ID: $interventionId and PduId $pduId\"",
        ),
      )
  }

  @Test
  fun `getCrsInterventionDetails return 404 when pdu is not found`() {
    val interventionId = UUID.fromString("3ccb511b-89b2-42f7-803b-304f54d85a24")
    val pduId = "INVALID_PDU"

    webTestClient.get()
      .uri { it.path("/intervention/$interventionId/pdu/$pduId").build() }
      .headers(setAuthorisation(roles = listOf(interventionClientRole)))
      .exchange()
      .expectStatus()
      .isNotFound
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody<ErrorResponse>()
      .isEqualTo(
        ErrorResponse(
          status = HttpStatus.NOT_FOUND,
          userMessage = "Intervention Not Found with ID: $interventionId and PduId $pduId",
          developerMessage = "404 NOT_FOUND \"Intervention Not Found with ID: $interventionId and PduId $pduId\"",
        ),
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
  }
}
