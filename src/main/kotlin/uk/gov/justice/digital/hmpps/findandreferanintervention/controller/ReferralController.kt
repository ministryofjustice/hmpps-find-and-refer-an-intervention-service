package uk.gov.justice.digital.hmpps.findandreferanintervention.controller

import com.microsoft.applicationinsights.TelemetryClient
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import uk.gov.justice.digital.hmpps.findandreferanintervention.config.logToAppInsights
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.ReferralDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.ReferralService
import java.util.UUID

@RestController
@PreAuthorize("hasRole('ROLE_ACCREDITED_PROGRAMMES_MANAGE_AND_DELIVER_API__ACPMAD_UI_WR')")
class ReferralController(
  private val referralService: ReferralService,
  private val telemetryClient: TelemetryClient,
) {

  @GetMapping(
    "/referral/{referralId}",
    produces = [MediaType.APPLICATION_JSON_VALUE],
    name = "Get a Referral by Referral Id",
  )
  @ApiResponse(responseCode = "200", description = "OK")
  fun getReferralDetails(@PathVariable referralId: UUID): ReferralDetailsDto? {
    telemetryClient.logToAppInsights(
      "Received request for referral details",
      mapOf("referralId" to referralId.toString()),
    )
    return referralService.getReferralDetailsById(referralId) ?: throw ResponseStatusException(
      HttpStatus.NOT_FOUND,
      "Referral Not Found with ID: $referralId",
    )
  }
}
