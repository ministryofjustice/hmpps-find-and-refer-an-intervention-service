package uk.gov.justice.digital.hmpps.findandreferanintervention.controller

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.logToAppInsights
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.InterventionService
import java.util.UUID

@RestController
@PreAuthorize("hasRole('ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR')")
class InterventionController(
  private val interventionService: InterventionService,
  private val telemetryClient: TelemetryClient,
) {
  @GetMapping("/interventions/{setting}", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getInterventionsCatalogue(
    @PageableDefault(page = 0, size = 10) pageable: Pageable,
    @RequestParam(name = "allowsFemales", required = false)
    allowsFemales: Boolean?,
    @RequestParam(name = "allowsMales", required = false)
    allowsMales: Boolean?,
    @RequestParam(name = "interventionType", required = false)
    interventionTypes: List<InterventionType>?,
    @PathVariable(name = "setting", required = true)
    settingType: SettingType,
  ): Page<InterventionCatalogueDto> {
    telemetryClient.logToAppInsights(
      "InterventionsCatalogue Summary",
      mapOf(
        "userMessage" to "User has hit interventions catalogue summary page",
      ),
    )

    return interventionService.getInterventionsCatalogueByCriteria(
      pageable,
      interventionTypes,
      settingType,
      allowsMales,
      allowsFemales,
    )
  }

  @GetMapping("/interventions/details/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun getInterventionDetails(@PathVariable id: UUID): InterventionDetailsDto? {
    telemetryClient.logToAppInsights(
      "InterventionsDetail page",
      mapOf("userMessage" to "User has hit interventions details page", "interventionId" to id.toString()),
    )
    return interventionService.getInterventionDetailsById(id) ?: throw ResponseStatusException(
      HttpStatus.NOT_FOUND,
      "Intervention Not Found with ID: $id",
    )
  }
}
