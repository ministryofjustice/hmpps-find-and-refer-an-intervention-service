package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
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
  val suitableForPeopleWithLearningDifficulties: Boolean?,
  val equivalentNonLdcProgramme: String?,
  val timeToComplete: String?,
  val deliveryFormat: List<String>,
  val attendanceType: List<String>,
  val description: String,
  val eligibility: String,
  val outcomes: String,
  val sessionDetails: String?,
  val communityLocations: List<CommunityLocation>?,
  val custodyLocations: List<CustodyLocation>?,
) {
  companion object {
    fun fromEntity(
      interventionCatalogue: InterventionCatalogue,
    ): InterventionDetailsDto {
      val deliveryMethodDtos =
        interventionCatalogue.deliveryMethods.map { DeliveryMethodDto.fromEntity(it) }
      return InterventionDetailsDto(
        id = interventionCatalogue.id,
        criminogenicNeeds =
        interventionCatalogue.criminogenicNeeds.map {
          CriminogenicNeedDto.fromEntity(it).need
        },
        title = interventionCatalogue.name,
        description = interventionCatalogue.shortDescription,
        interventionType = interventionCatalogue.interventionType,
        allowsMales = interventionCatalogue.personalEligibility?.males!!,
        allowsFemales = interventionCatalogue.personalEligibility?.females!!,
        riskCriteria =
        interventionCatalogue.riskConsideration?.let {
          RiskConsiderationDto.fromEntity(it).listOfRisks()
        },
        attendanceType = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.attendanceType },
        deliveryFormat = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.deliveryFormat },
        timeToComplete = interventionCatalogue.timeToComplete,
        suitableForPeopleWithLearningDifficulties = interventionCatalogue.specialEducationalNeeds?.learningDisabilityCateredFor,
        equivalentNonLdcProgramme = interventionCatalogue.specialEducationalNeeds?.equivalentNonLdcProgrammeGuide,
        minAge = interventionCatalogue.personalEligibility?.minAge,
        maxAge = interventionCatalogue.personalEligibility?.maxAge,
        eligibility = "Eligibilty text",
        outcomes = "Out come text",
        sessionDetails = interventionCatalogue.sessionDetail,
        communityLocations = listOf(),
        custodyLocations = listOf(),
      )
    }

    data class CommunityLocation(val name: String)

    data class CustodyLocation(val name: String, val category: String, val county: String)
  }
}
