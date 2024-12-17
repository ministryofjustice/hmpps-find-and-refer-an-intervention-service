package uk.gov.justice.digital.hmpps.findandreferanintervention.integration

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.wiremock.HmppsAuthApiExtension
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.hmpps.test.kotlin.auth.JwtAuthorisationHelper

@ExtendWith(HmppsAuthApiExtension::class) // , ExampleApiExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integration")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [IntegrationTestBase.Initializer::class])
abstract class IntegrationTestBase {

  companion object {
    @JvmStatic
    private val dockerImageName: DockerImageName =
      DockerImageName.parse("postgres:16").asCompatibleSubstituteFor("postgres")

    @JvmStatic
    private val postgresContainer: PostgreSQLContainer<*> =
      PostgreSQLContainer(dockerImageName).apply {
        withDatabaseName("findandrefer")
        withUsername("postgres")
        withPassword("password")
      }.waitingFor(Wait.forListeningPort())
  }

  class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
      postgresContainer.start()
      TestPropertyValues.of(
        mapOf(
          "spring.flyway.url" to postgresContainer.jdbcUrl,
          "spring.flyway.user" to postgresContainer.username,
          "spring.flyway.password" to postgresContainer.password,
          "spring.datasource.url" to postgresContainer.jdbcUrl,
          "spring.datasource.user" to postgresContainer.username,
          "spring.datasource.password" to postgresContainer.password,
        ),
      ).applyTo(applicationContext)
    }
  }

  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthorisationHelper

  internal fun setAuthorisation(
    username: String? = "AUTH_ADM",
    roles: List<String> = listOf(),
    scopes: List<String> = listOf("read"),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisationHeader(username = username, scope = scopes, roles = roles)

  protected fun stubPingWithResponse(status: Int) {
    hmppsAuth.stubHealthPing(status)
  }
}
