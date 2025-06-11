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
  val criminogenicNeedsScore: String? = null,
  val eligibleOffence: String? = null,
  val enablingIntervention: EnablingInterventionDto? = null,
  val interventionType: InterventionType,
  val title: String,
  val minAge: Int? = null,
  val maxAge: Int? = null,
  val expectedOutcomes: List<String>? = null,
  val riskCriteria: RiskConsiderationDto? = null,
  val suitableForPeopleWithLearningDifficulties: String? = null,
  val equivalentNonLdcProgramme: String? = null,
  val timeToComplete: String? = null,
  val deliveryFormat: String? = null,
  val attendanceType: String? = null,
  val description: List<String>? = null,
  val sessionDetails: String? = null,
  val communityLocations: List<CommunityLocation>? = null,
  val custodyLocations: List<CustodyLocation>? = null,
  val exclusion: ExclusionDto? = null,
) {
  data class CommunityLocation(val npsRegion: String, val pdus: MutableSet<PduRefDto>)

  data class CustodyLocation(val prisonName: String, val category: String?, val county: String?)
}

fun InterventionCatalogue.toDetailsDto(): InterventionDetailsDto {
  val deliveryLocationDtos = this.deliveryLocations.map { it.toDto() }
  val courseDtos = this.courses.map { it.toDto() }

  return InterventionDetailsDto(
    id = this.id,
    criminogenicNeeds =
    this.criminogenicNeeds.map {
      CriminogenicNeedDto.fromEntity(it).need
    }.sorted(),
    criminogenicNeedsScore = riskConsideration?.cnScoreGuide,
    title = this.name,
    description = this.longDescription?.map { it } ?: listOf(this.shortDescription),
    enablingIntervention = this.enablingIntervention?.toDto(),
    interventionType = this.interventionType,
    allowsMales = this.personalEligibility?.males!!,
    allowsFemales = this.personalEligibility?.females!!,
    riskCriteria = this.riskConsideration?.toDto(),
    attendanceType = this.deliveryMethod?.attendanceType,
    deliveryFormat = this.deliveryMethod?.deliveryFormat,
    timeToComplete = this.timeToComplete,
    suitableForPeopleWithLearningDifficulties = this.specialEducationalNeeds?.learningDisabilityCateredFor,
    equivalentNonLdcProgramme = this.specialEducationalNeeds?.equivalentNonLdcProgrammeGuide,
    minAge = this.personalEligibility?.minAge,
    maxAge = this.personalEligibility?.maxAge,
    expectedOutcomes = this.possibleOutcomes.map { it.outcome }.ifEmpty { null },
    sessionDetails = this.sessionDetail,
    communityLocations = getCommunityLocations(deliveryLocationDtos)?.sortedBy { it.npsRegion },
    custodyLocations = getCustodyLocations(courseDtos)?.sortedBy { it.prisonName },
    exclusion = this.exclusion?.toDto(),
  )
}

private fun getCommunityLocations(deliveryLocations: List<DeliveryLocationDto>): List<CommunityLocation>? = deliveryLocations.groupBy { it.pduRef.pccRegion.npsRegion }.map { npsRegionDto ->
  CommunityLocation(
    npsRegionDto.key.name,
    npsRegionDto.value.map { it.pduRef }.toMutableSet(),
  )
}.ifEmpty { null }

private fun getCustodyLocations(courseDtos: List<CourseDto>): List<CustodyLocation>? = courseDtos.flatMap { courseDto ->
  val offerings = courseDto.offering
  val organisations = offerings.map { offering -> offering.organisation }
  organisations
}.toMutableSet().map { CustodyLocation(it.name, it.category, it.county) }.ifEmpty { null }
