package uk.gov.justice.digital.hmpps.findandreferanintervention.event.listener

import java.util.UUID

sealed class ReferralEvent(
  open val referralId: UUID,
)

data class CommunityReferralCreatedEvent(override val referralId: UUID) : ReferralEvent(referralId)
