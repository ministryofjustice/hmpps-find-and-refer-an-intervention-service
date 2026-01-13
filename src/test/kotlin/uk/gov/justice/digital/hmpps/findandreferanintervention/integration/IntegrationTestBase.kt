package uk.gov.justice.digital.hmpps.findandreferanintervention.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.transaction.annotation.EnableTransactionManagement
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.HmppsDomainEvent
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.container.LocalStackContainer
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.container.LocalStackContainer.setLocalStackProperties
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.wiremock.HmppsAuthApiExtension
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.hmpps.sqs.HmppsQueue
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import uk.gov.justice.hmpps.sqs.MissingTopicException
import uk.gov.justice.hmpps.sqs.countAllMessagesOnQueue
import uk.gov.justice.hmpps.sqs.publish
import uk.gov.justice.hmpps.test.kotlin.auth.JwtAuthorisationHelper

@ExtendWith(HmppsAuthApiExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integration")
@EnableTransactionManagement
abstract class IntegrationTestBase {

  @Autowired
  lateinit var objectMapper: ObjectMapper

  @Autowired
  protected lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthorisationHelper

  @MockitoSpyBean
  lateinit var hmppsQueueService: HmppsQueueService

  internal val hmppsDomainEventsQueue by lazy {
    hmppsQueueService.findByQueueId("hmppsdomaineventsqueue")
      ?: throw MissingQueueException("hmppsdomaineventsqueue queue not found")
  }

  internal val hmppsEventTopic by lazy {
    hmppsQueueService.findByTopicId("hmppseventtopic")
      ?: throw MissingQueueException("HmppsTopic hmpps event topic not found")
  }

  internal val interventionsQueue by lazy {
    hmppsQueueService.findByQueueId("interventionseventtestqueue")
      ?: throw MissingQueueException("interventionseventtestqueue queue not found")
  }

  val domainEventsTopic by lazy {
    hmppsQueueService.findByTopicId("hmppseventtopic") ?: throw MissingTopicException("hmppseventtopic not found")
  }

  internal fun sendDomainEvent(event: HmppsDomainEvent) {
    domainEventsTopic.publish(event.eventType, objectMapper.writeValueAsString(event))
  }

  @BeforeEach
  fun `clear queues`() {
    hmppsDomainEventsQueue.sqsClient.purgeQueue(
      PurgeQueueRequest.builder().queueUrl(hmppsDomainEventsQueue.queueUrl).build(),
    )
      .get()
    interventionsQueue.sqsClient.purgeQueue(
      PurgeQueueRequest.builder().queueUrl(interventionsQueue.queueUrl).build(),
    )
      .get()
  }

  internal fun HmppsQueue.countAllMessagesOnQueue() = sqsClient.countAllMessagesOnQueue(queueUrl).get()

  internal fun HmppsQueue.receiveMessageOnQueue() = sqsClient.receiveMessage(ReceiveMessageRequest.builder().queueUrl(queueUrl).build()).get().messages().single()

  internal fun setAuthorisation(
    username: String? = "AUTH_ADM",
    roles: List<String> = listOf(),
    scopes: List<String> = listOf("read"),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisationHeader(username = username, scope = scopes, roles = roles)

  protected fun stubPingWithResponse(status: Int) {
    hmppsAuth.stubHealthPing(status)
  }

  companion object {
    private val localStackContainer = LocalStackContainer.instance

    @JvmStatic
    @DynamicPropertySource
    fun properties(registry: DynamicPropertyRegistry) {
      System.setProperty("aws.region", "eu-west-2")

      localStackContainer?.also { setLocalStackProperties(it, registry) }
    }
  }
}
