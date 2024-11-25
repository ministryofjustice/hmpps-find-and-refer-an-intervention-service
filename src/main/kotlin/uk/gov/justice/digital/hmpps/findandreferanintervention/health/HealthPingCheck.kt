package uk.gov.justice.digital.hmpps.findandreferanintervention.health

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.hmpps.kotlin.health.HealthPingCheck

// HMPPS Auth health ping is required if your service calls HMPPS Auth to get a token to call other services
@Component("hmppsAuth")
class HmppsAuthHealthPing(@Qualifier("hmppsAuthHealthWebClient") webClient: WebClient) : HealthPingCheck(webClient)

@Component
class HealthInfo(buildProperties: BuildProperties) : HealthIndicator {
  private val version: String = buildProperties.version

  override fun health(): Health = Health.up().withDetail("version", version).build()
}

// TODO: Example ping health check calling out to other services
// @Component("exampleApi")
// class ExampleApiHealthPing(@Qualifier("exampleApiHealthWebClient") webClient: WebClient) : HealthPingCheck(webClient)
