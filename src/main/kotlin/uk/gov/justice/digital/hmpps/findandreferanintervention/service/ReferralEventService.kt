package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.DomainEventPublisher
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.HmppsDomainEvent
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.PersonReference
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.ReferralRepository
import java.time.ZonedDateTime
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ReferralEventService(
  private val referralRepository: ReferralRepository,
  private val domainEventPublisher: DomainEventPublisher,
  @Value($$"${api.baseurl.find-and-refer}") private val findAndReferBaseUrl: String,
) {
  private val logger = LoggerFactory.getLogger(this::class.java)

  fun publishCommunityReferralCreatedEvent(referralId: UUID) {
    val referral = referralRepository.findReferralById(referralId)
      ?: return logger.warn("Referral with id: $referralId not found. Unable to publish community-referral.created event")

    val hmppsDomainEvent = HmppsDomainEvent(
      eventType = "interventions.community-referral.created",
      version = 1,
      detailUrl = "$findAndReferBaseUrl/referral/$referralId",
      occurredAt = ZonedDateTime.now(),
      description = "An Interventions referral in community has been created.",
      additionalInformation = mutableMapOf(),
      personReference = PersonReference(
        listOf(PersonReference.Identifier(referral.personReferenceType.name, referral.personReference)),
      ),
    )
    logger.info("Publishing interventions.community-referral.created event for referralId: $referralId")
    domainEventPublisher.publish(hmppsDomainEvent)
  }
}
