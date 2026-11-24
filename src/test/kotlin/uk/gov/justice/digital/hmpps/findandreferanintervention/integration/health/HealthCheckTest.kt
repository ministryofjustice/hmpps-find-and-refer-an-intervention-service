package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.health

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth

class HealthCheckTest : IntegrationTestBase() {

  @Value("\${hmpps-auth.baseurl}")
  private lateinit var authBaseUrl: String

  @Value("\${spring.security.oauth2.client.provider.hmpps-auth.token-uri}")
  private lateinit var tokenUri: String

  @Test
  fun `auth mock URLs use the running server`() {
    hmppsAuth.stubGrantToken()

    assertThat(authBaseUrl).isEqualTo("http://localhost:${hmppsAuth.port()}/auth")
    assertThat(tokenUri).isEqualTo("$authBaseUrl/oauth/token")

    WebTestClient.bindToServer().build().post()
      .uri(tokenUri)
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("access_token").isEqualTo("ABCDE")
  }

  @Test
  fun `Health page reports ok`() {
    stubPingWithResponse(200)

    webTestClient.get()
      .uri("/health")
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("status").isEqualTo("UP")
  }

  @Test
  fun `Health ping page is accessible`() {
    webTestClient.get()
      .uri("/health/ping")
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("status").isEqualTo("UP")
  }

  @Test
  fun `readiness reports ok`() {
    webTestClient.get()
      .uri("/health/readiness")
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("status").isEqualTo("UP")
  }

  @Test
  fun `liveness reports ok`() {
    webTestClient.get()
      .uri("/health/liveness")
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("status").isEqualTo("UP")
  }
}
