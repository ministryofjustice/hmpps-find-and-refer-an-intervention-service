package uk.gov.justice.digital.hmpps.findandreferanintervention.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.publish

@Service
class DomainEventPublisher(
  private val hmppsQueueService: HmppsQueueService,
  private val objectMapper: ObjectMapper,
) {
  private val domainEventsTopic by lazy {
    hmppsQueueService.findByTopicId("hmppseventtopic") ?: throw IllegalStateException("hmppseventtopic not found")
  }

  fun publishSingle(domainEvent: DomainEvent) {
    domainEventsTopic.publish(domainEvent.eventType, objectMapper.writeValueAsString(domainEvent))
  }
}
