package uk.gov.justice.digital.hmpps.findandreferanintervention.controller

import com.microsoft.applicationinsights.TelemetryClient
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.constraints.Pattern
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.logToAppInsights
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.ServiceUserDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ServiceUserService

@RestController
@PreAuthorize("hasAnyRole('ROLE_FIND_AND_REFER_AN_INTERVENTION_API__FAR_UI__WR')")
class ServiceUserController(
  private val service: ServiceUserService,
  private val telemetryClient: TelemetryClient,
) {

  @GetMapping(
    "/service-user/{identifier}",
    produces = [MediaType.APPLICATION_JSON_VALUE],
    name = "Get Service User for the CRN or the Prison Number.",
  )
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(responseCode = "400", description = "Bad Request"),
    ],
  )
  fun serviceUserByCrnOrPrisonerNumber(
    @PathVariable(name = "identifier")
    @Pattern(regexp = "^([A-Z]\\d{6}|[A-Z]\\d{4}[A-Z]{2})$", message = "Invalid code format. Expected format for CRN: X718255 or PrisonNumber: A1234AA")
    identifier: String,
  ): ServiceUserDto {
    telemetryClient.logToAppInsights(
      "retrieve service user details",
      mapOf(
        "userMessage" to "User has hit service user details endpoint",
        "identifier" to identifier,
      ),
    )
    return service.getServiceUserByIdentifier(identifier)
      ?: throw ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Service user with identifier $identifier not found",
      )
  }
}
