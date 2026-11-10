package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.container

import org.slf4j.LoggerFactory
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import java.io.IOException
import java.net.ServerSocket

object PostgresContainer {
  private val log = LoggerFactory.getLogger(this::class.java)
  val instance by lazy { startPostgresIfNotRunning() }

  fun setPostgresProperties(container: PostgreSQLContainer<*>, registry: DynamicPropertyRegistry) {
    registry.add("postgres.uri") { "${container.host}:${container.getMappedPort(5432)}" }
    registry.add("postgres.db") { container.databaseName }
    registry.add("postgres.username") { container.username }
    registry.add("postgres.password") { container.password }
  }

  private fun startPostgresIfNotRunning(): PostgreSQLContainer<*>? {
    if (postgresIsRunning()) return null
    val logConsumer = Slf4jLogConsumer(log).withPrefix("postgres")
    return PostgreSQLContainer("postgres:18").apply {
      withDatabaseName("postgres")
      withUsername("root")
      withPassword("dev")
      withLogConsumer(logConsumer)
      start()
    }
  }

  private fun postgresIsRunning(): Boolean = try {
    ServerSocket(5432).use { false }
  } catch (e: IOException) {
    true
  }
}
