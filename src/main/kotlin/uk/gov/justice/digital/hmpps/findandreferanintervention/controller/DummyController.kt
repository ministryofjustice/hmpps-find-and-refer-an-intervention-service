package uk.gov.justice.digital.hmpps.findandreferanintervention.controller

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.logToAppInsights
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Dummy
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DummyRepository

@RestController
@PreAuthorize("hasRole('ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR')")
class DummyController(
  private val dummyRepository: DummyRepository,
  private val telemetryClient: TelemetryClient,
) {

  @GetMapping("/dummy/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getDummy(@PathVariable id: Int): Dummy? {
    telemetryClient.logToAppInsights(
      "LoggingToAppInsights",
      mapOf(
        "userMessage" to "User has hit the dummy endpoint",
        "dummyId" to id.toString(),
      ),
    )
    return dummyRepository.findByDummyId(id)
  }
}
