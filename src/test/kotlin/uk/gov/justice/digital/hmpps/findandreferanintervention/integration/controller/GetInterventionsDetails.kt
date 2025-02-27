package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.controller

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import java.util.UUID

internal class GetInterventionsDetails : IntegrationTestBase() {

  @Test
  fun `getInterventionsDetails for CRS`() {
    val id = UUID.fromString("4902a268-9907-4070-ba48-7c2870a3b77e")
    val response = webTestClient.get()
      .uri { it.path("/interventions/details/$id").build() }
      .headers(setAuthorisation(roles = listOf("ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR")))
      .exchange()
      .expectStatus()
      .isOk
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBody()
  }
}
