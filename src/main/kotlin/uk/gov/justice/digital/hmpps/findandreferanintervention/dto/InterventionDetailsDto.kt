package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import java.util.UUID

// Excludes any properties that have null values when creating dto.
@JsonInclude(JsonInclude.Include.NON_NULL)
data class InterventionDetailsDto(
  val id: UUID,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val criminogenicNeeds: List<String>,
  val interventionType: InterventionType,
  val title: String,
  val minAge: Int?,
  val maxAge: Int?,
  val riskCriteria: List<String>?,
  val suitableForPeopleWithLearningDifficulties: String?,
  val equivalentNonLdcProgramme: String?,
  val timeToComplete: String?,
  val deliveryFormat: List<String>,
  val attendanceType: List<String>,
  val description: String,
  val sessionDetails: String?,
  val communityLocations: List<CommunityLocation>?,
  val custodyLocations: List<CustodyLocation>?,
) {
  data class CommunityLocation(val name: String, val locations: List<String>)

  data class CustodyLocation(val name: String, val category: String, val county: String)
}
