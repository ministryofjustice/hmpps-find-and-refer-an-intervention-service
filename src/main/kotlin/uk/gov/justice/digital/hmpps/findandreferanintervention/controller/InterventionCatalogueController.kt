package uk.gov.justice.digital.hmpps.findandreferanintervention.controller

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.InterventionCatalogueService

@RestController
@PreAuthorize("hasRole('ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR')")
class InterventionCatalogueController(
  private val interventionCatalogueService: InterventionCatalogueService,
  private val telemetryClient: TelemetryClient,
) {
  @GetMapping("/interventions", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getInterventionsCatalogue(
    @PageableDefault(page = 0, size = 10) pageable: Pageable,
  ): Page<InterventionCatalogueDto> {
    logToAppInsights()
    return interventionCatalogueService.getInterventionsCatalogue(pageable)
  }

  fun logToAppInsights() {
    telemetryClient.trackEvent(
      "InterventionsCatalogue Summary",
      mapOf(
        "userMessage" to "User has hit interventions catalogue summary page",
      ),
      null,
    )
  }
}
