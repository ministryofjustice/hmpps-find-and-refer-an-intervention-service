package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.event

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.awaitility.kotlin.withPollDelay
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.jdbc.datasource.init.ScriptUtils
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SourcedFromReferenceType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.MessageRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.ReferralRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.event.DomainEventsListener.Companion.LICENCE_CONDITION_CREATED
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.event.DomainEventsListener.Companion.REQUIREMENT_CREATED
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.events.HmppsDomainEventsFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.events.createLicenceConditionCreatedEvent
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.events.createRequirementCreatedEvent
import java.time.Duration.ofSeconds
import javax.sql.DataSource

class HandleProbationCaseEvents : IntegrationTestBase() {

  @Autowired
  private lateinit var referralRepository: ReferralRepository

  @Autowired
  private lateinit var messageRepository: MessageRepository

  @Autowired
  private lateinit var dataSource: DataSource

  @Autowired
  private lateinit var resourceLoader: ResourceLoader

  private val hmppsDomainEventsFactory = HmppsDomainEventsFactory()

  @BeforeEach
  fun beforeEach() {
    dataSource.connection.use {
      val r = resourceLoader.getResource("classpath:testData/setup.sql")
      ScriptUtils.executeSqlScript(it, r)
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
  fun `handle probation-case requirement created event`() {
    sendDomainEvent(hmppsDomainEventsFactory.createRequirementCreatedEvent())
    // Wait for message to be processed
    await withPollDelay ofSeconds(1) untilCallTo { hmppsDomainEventsQueue.countAllMessagesOnQueue() } matches { it == 0 }
    assertThat(referralRepository.count()).isEqualTo(1)
    val referral = referralRepository.findAll().first()
    assertThat(referral.interventionName).isEqualTo("Breaking Free Online")
    assertThat(referral.sourcedFromReference).isEqualTo("2500812305")
    assertThat(referral.sourcedFromReferenceType).isEqualTo(SourcedFromReferenceType.REQUIREMENT)

    val message = messageRepository.findAll().first()
    assertThat(message.event.eventType).isEqualTo(REQUIREMENT_CREATED)
    assertThat(message.referral!!.id).isEqualTo(referral.id)
  }

  @Test
  fun `handle probation-case licence-condition created event`() {
    sendDomainEvent(hmppsDomainEventsFactory.createLicenceConditionCreatedEvent())
    // Wait for message to be processed
    await withPollDelay ofSeconds(1) untilCallTo { hmppsDomainEventsQueue.countAllMessagesOnQueue() } matches { it == 0 }

    assertThat(referralRepository.count()).isEqualTo(1)
    val referral = referralRepository.findAll().first()
    assertThat(referral.interventionName).isEqualTo("Horizon")
    assertThat(referral.sourcedFromReference).isEqualTo("2500782763")
    assertThat(referral.sourcedFromReferenceType).isEqualTo(SourcedFromReferenceType.LICENCE_CONDITION)

    val message = messageRepository.findAll().first()
    assertThat(message.event.eventType).isEqualTo(LICENCE_CONDITION_CREATED)
    assertThat(message.referral!!.id).isEqualTo(referral.id)
  }

  @Test
  fun `when duplicate referral created request for Requirement Created event don't create referral`() {
    val duplicateEvent = hmppsDomainEventsFactory.createRequirementCreatedEvent()
    sendDomainEvent(duplicateEvent)
    sendDomainEvent(duplicateEvent)
    // Wait for message to be processed
    await withPollDelay ofSeconds(1) untilCallTo { hmppsDomainEventsQueue.countAllMessagesOnQueue() } matches { it == 0 }

    assertThat(referralRepository.count()).isEqualTo(1)
    assertThat(messageRepository.count()).isEqualTo(2)
  }

  @Test
  fun `when duplicate referral created request for Licence Condition created event don't create referral`() {
    val duplicateEvent = hmppsDomainEventsFactory.createLicenceConditionCreatedEvent()
    sendDomainEvent(duplicateEvent)
    sendDomainEvent(duplicateEvent)
    // Wait for message to be processed
    await withPollDelay ofSeconds(1) untilCallTo { hmppsDomainEventsQueue.countAllMessagesOnQueue() } matches { it == 0 }

    assertThat(referralRepository.count()).isEqualTo(1)
    assertThat(messageRepository.count()).isEqualTo(2)
  }
}
