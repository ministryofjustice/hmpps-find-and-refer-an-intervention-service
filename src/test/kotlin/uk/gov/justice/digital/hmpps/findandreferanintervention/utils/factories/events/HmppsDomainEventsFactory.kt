package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories.events

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.model.event.HmppsDomainEvent
import uk.gov.justice.digital.hmpps.findandreferanintervention.model.event.PersonReference
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
  personReference: PersonReference? = PersonReference(
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

fun HmppsDomainEventsFactory.createRequirementCreatedEvent() = this.create(
  eventType = "probation-case.requirement.created",
  additionalInformation = mapOf(Pair("requirementSubType", "Curfew")),
  occurredAt = ZonedDateTime.now(),
)

fun HmppsDomainEventsFactory.createLicenceConditionCreatedEvent() = this.create(
  eventType = "probation-case.licence-condition.created",
  additionalInformation = mapOf(Pair("licconditionMainType", "Polygraph Condition")),
  occurredAt = ZonedDateTime.now(),
)
