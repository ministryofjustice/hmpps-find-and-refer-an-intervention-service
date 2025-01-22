package uk.gov.justice.digital.hmpps.findandreferanintervention.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.InterventionCatalogueService

@RestController
@PreAuthorize("hasRole('ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR')")
class InterventionCatalogueController(
  private val interventionCatalogueService: InterventionCatalogueService,
//  private val telemetryClient: TelemetryClient,
) {
  @GetMapping("/interventions", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getInterventionsCatalogue(
    @RequestParam(defaultValue = "0") pageNo: Int,
    @RequestParam(defaultValue = "10") pageSize: Int,
  ): Page<InterventionCatalogueDto> {
//    logToAppInsights(id)
    val page = PageRequest.of(pageNo, pageSize)
    val interventions = interventionCatalogueService.getInterventionsCatalogue(page)
    return interventions
  }
  // TODO add app insights logging
//  fun logToAppInsights(id: Int) {
//    telemetryClient.trackEvent(
//      "LoggingToAppInsights",
//      mapOf(
//        "userMessage" to "User has hit the dummy endpoint",
//        "dummyId" to id.toString(),
//      ),
//      null,
//    )
//  }
}
