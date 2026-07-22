package uk.gov.justice.digital.hmpps.findandreferanintervention.event.listener

import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ReferralEventService

/**
 * This listener ensures that we only publish our referral events AFTER we have committed a transaction
 */
@Component
class ReferralEventListener(private val referralEventService: ReferralEventService) {

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  fun onCommunityReferralCreated(event: CommunityReferralCreatedEvent) {
    referralEventService.publishCommunityReferralCreatedEvent(event.referralId)
  }
}
