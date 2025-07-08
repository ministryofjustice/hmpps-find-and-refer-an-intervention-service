package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.events

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.HmppsDomainEvent
import uk.gov.justice.digital.hmpps.findandreferanintervention.event.PersonReference
import uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.EntityFactory
import java.time.ZonedDateTime

class HmppsDomainEventsFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun HmppsDomainEventsFactory.create(
  eventType: String = "EVENT_TYPE",
  version: Int = 1,
  detailUrl: String? = null,
  occurredAt: ZonedDateTime = ZonedDateTime.now(),
  description: String? = null,
  additionalInformation: Map<String, Any?> = mutableMapOf(),
  personReference: PersonReference = PersonReference(
    listOf(
      PersonReference.Identifier(
        type = "CRN",
        value = "X928972",
      ),
    ),
  ),
): HmppsDomainEvent = save(
  HmppsDomainEvent(
    eventType,
    version,
    detailUrl,
    occurredAt,
    description,
    additionalInformation,
    personReference,
  ),
)

fun HmppsDomainEventsFactory.createRequirementCreatedEvent(
  requirementSubType: String? = "Breaking Free Online",
  requirementMainType: String? = "Court - Accredited Programme",
  requirementID: String? = "2500812305",
  eventNumber: String? = "1",
  restrictiveRequirement: String? = "false",
) = this.create(
  eventType = "probation-case.requirement.created",
  additionalInformation = mapOf(
    Pair("requirementSubType", requirementSubType),
    Pair("requirementMainType", requirementMainType),
    Pair("requirementID", requirementID),
    Pair("eventNumber", eventNumber),
    Pair("restrictiveRequirement", restrictiveRequirement),
  ),
  occurredAt = ZonedDateTime.now(),
)

fun HmppsDomainEventsFactory.createLicenceConditionCreatedEvent(
  licconditionSubType: String? = "Horizon",
  licconditionMainType: String? = "Licence - Accredited Programme",
  licconditionId: String? = "2500782763",
  eventNumber: String? = "1",
) = this.create(
  eventType = "probation-case.licence-condition.created",
  additionalInformation = mapOf(
    Pair("licconditionSubType", licconditionSubType),
    Pair("licconditionMainType", licconditionMainType),
    Pair("licconditionId", licconditionId),
    Pair("eventNumber", eventNumber),
  ),
  occurredAt = ZonedDateTime.now(),
)
