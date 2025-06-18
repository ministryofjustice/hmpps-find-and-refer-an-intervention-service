package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.event

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.awaitility.kotlin.withPollDelay
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.event.DomainEventsListener
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.events.HmppsDomainEventsFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.events.createLicenceConditionCreatedEvent
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.events.createRequirementCreatedEvent
import java.time.Duration.ofSeconds

class DomainEventListenerTest : IntegrationTestBase() {

  private val hmppsDomainEventsFactory = HmppsDomainEventsFactory()

  val logger = LoggerFactory.getLogger(DomainEventsListener::class.java) as Logger
  val listAppender = ListAppender<ILoggingEvent>()

  @BeforeEach
  fun beforeEach() {
    listAppender.start()
    logger.addAppender(listAppender)
  }

  @AfterEach
  fun afterEach() {
    listAppender.stop()
  }

  @Test
  fun `handle probation-case requirement created event`() {
    sendDomainEvent(hmppsDomainEventsFactory.createRequirementCreatedEvent())
    // Wait for message to be processed
    await withPollDelay ofSeconds(1) untilCallTo { hmppsDomainEventsQueue.countAllMessagesOnQueue() } matches { it == 0 }

    val logMessages = listAppender.list.map { it.formattedMessage }
    assertThat(logMessages.first()).contains("probation-case.requirement.created event")
  }

  @Test
  fun `handle probation-case licence-condition created event`() {
    sendDomainEvent(hmppsDomainEventsFactory.createLicenceConditionCreatedEvent())
    // Wait for message to be processed
    await withPollDelay ofSeconds(1) untilCallTo { hmppsDomainEventsQueue.countAllMessagesOnQueue() } matches { it == 0 }

    val logMessages = listAppender.list.map { it.formattedMessage }
    assertThat(logMessages.first()).contains("probation-case.licence-condition.created event")
  }
}
