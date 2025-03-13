package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionDetailsDto.CommunityLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionDetailsDto.CustodyLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.toDto
import java.util.UUID

// Excludes any properties that have null values when creating dto.
@JsonInclude(JsonInclude.Include.NON_NULL)
data class InterventionDetailsDto(
  val id: UUID,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val criminogenicNeeds: List<String>? = null,
  val interventionType: InterventionType,
  val title: String,
  val minAge: Int? = null,
  val maxAge: Int? = null,
  val expectedOutcomes: List<String>? = null,
  val riskCriteria: List<String>? = null,
  val suitableForPeopleWithLearningDifficulties: String? = null,
  val equivalentNonLdcProgramme: String? = null,
  val timeToComplete: String? = null,
  val deliveryFormat: List<String>? = null,
  val attendanceType: List<String>? = null,
  val description: String,
  val sessionDetails: String? = null,
  val communityLocations: List<CommunityLocation>? = null,
  val custodyLocations: List<CustodyLocation>? = null,
) {
  data class CommunityLocation(val pccRegion: String, val pduRefs: MutableSet<PduRefDto>)

  data class CustodyLocation(val prisonName: String, val category: String?, val county: String?)
}

fun InterventionCatalogue.toDetailsDto(): InterventionDetailsDto {
  val deliveryMethodDtos =
    this.deliveryMethods.map { DeliveryMethodDto.fromEntity(it) }
  val interventionsDtos = this.interventions.map { it.toDto() }
  val courseDtos = this.courses.map { it.toDto() }

  return InterventionDetailsDto(
    id = this.id,
    criminogenicNeeds =
    this.criminogenicNeeds.map {
      CriminogenicNeedDto.fromEntity(it).need
    },
    title = this.name,
    description = this.shortDescription,
    interventionType = this.interventionType,
    allowsMales = this.personalEligibility?.males!!,
    allowsFemales = this.personalEligibility?.females!!,
    riskCriteria =
    this.riskConsideration?.let {
      RiskConsiderationDto.fromEntity(it).listOfRisks()
    },
    attendanceType = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.attendanceType }.ifEmpty { null },
    deliveryFormat = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.deliveryFormat }.ifEmpty { null },
    timeToComplete = this.timeToComplete,
    suitableForPeopleWithLearningDifficulties = this.specialEducationalNeeds?.learningDisabilityCateredFor,
    equivalentNonLdcProgramme = this.specialEducationalNeeds?.equivalentNonLdcProgrammeGuide,
    minAge = this.personalEligibility?.minAge,
    maxAge = this.personalEligibility?.maxAge,
    expectedOutcomes = this.possibleOutcomes.map { it.outcome }.ifEmpty { null },
    sessionDetails = this.sessionDetail,
    communityLocations = getCommunityLocations(interventionsDtos)?.sortedBy { it.pccRegion },
    custodyLocations = getCustodyLocations(courseDtos)?.sortedBy { it.prisonName },
  )
}

private fun getCommunityLocations(interventionsDtos: List<InterventionDto>): List<CommunityLocation>? {
  return interventionsDtos.map { interventionDto ->
    val contract = interventionDto.dynamicFrameworkContract
    return if (contract.npsRegion != null) {
      contract.npsRegion.pccRegions.map { region ->
        CommunityLocation(
          region.name,
          region.pduRefs,
        )
      }
    } else if (contract.pccRegion != null) {
      val pduRefsPerPcc = contract.pccRegion.pduRefs
      pduRefsPerPcc.map { CommunityLocation(contract.pccRegion.name, pduRefsPerPcc) }
    } else {
      null
    }
  }.ifEmpty { null }
}

private fun getCustodyLocations(courseDtos: List<CourseDto>): List<CustodyLocation>? = courseDtos.flatMap { courseDto ->
  val offerings = courseDto.offering
  val organisations = offerings.map { offering -> offering.organisation }
  organisations
}.toMutableSet().map { CustodyLocation(it.name, it.category, it.county) }.ifEmpty { null }
