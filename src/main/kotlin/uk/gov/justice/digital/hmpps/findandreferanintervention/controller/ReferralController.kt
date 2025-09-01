package uk.gov.justice.digital.hmpps.findandreferanintervention.controller

import com.microsoft.applicationinsights.TelemetryClient
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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

@PreAuthorize("hasRole('ROLE_ACCREDITED_PROGRAMMES_MANAGE_AND_DELIVER_API__ACPMAD_UI_WR')")
@RestController
@Tag(name = "Referrals", description = "API for managing intervention referrals")
class ReferralController(
  private val referralService: ReferralService,
  private val telemetryClient: TelemetryClient,
) {

  @GetMapping(
    "/referral/{referralId}",
    produces = [MediaType.APPLICATION_JSON_VALUE],
    name = "Get a Referral by Referral Id",
  )
  @Operation(
    summary = "Get referral details by ID",
    description = "Retrieves detailed information about a specific referral including probation case information.  Used primarily by the Manage & Deliver service, in response to an Event.",
  )
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Referral details found and returned successfully",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ReferralDetailsDto::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "404",
        description = "Referral not found with the provided ID",
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized - authentication required",
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden - insufficient permissions",
      ),
    ],
  )
  fun getReferralDetails(
    @Parameter(
      description = "The unique identifier (UUID) of the referral to retrieve",
      required = true,
      example = "c0edf32e-c7be-5625-a971-9201b244e9e2",
    )
    @PathVariable referralId: UUID,
  ): ReferralDetailsDto? {
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
