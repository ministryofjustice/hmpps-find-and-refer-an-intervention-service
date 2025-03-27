package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import java.util.UUID

// Excludes any properties that have null values when creating dto.
@JsonInclude(JsonInclude.Include.NON_NULL)
data class InterventionCatalogueDto(
  val id: UUID,
  val criminogenicNeeds: List<String>,
  val title: String,
  val description: String,
  val interventionType: InterventionType,
  val setting: List<SettingType>,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val riskCriteria: List<String>?,
  val attendanceType: List<String>,
  val deliveryFormat: List<String>,
  val timeToComplete: String?,
  val suitableForPeopleWithLearningDifficulties: String?,
  val equivalentNonLdcProgramme: String?,
)

fun InterventionCatalogue.toDto(): InterventionCatalogueDto {
  val deliveryMethodDtos =
    this.deliveryMethods.map { DeliveryMethodDto.fromEntity(it) }
  val deliveryMethodSettingList =
    deliveryMethodDtos.flatMap { methodDto ->
      methodDto.deliveryMethodSettings.map { settingDto -> settingDto.setting }
    }
  return InterventionCatalogueDto(
    id = this.id,
    criminogenicNeeds =
    this.criminogenicNeeds.map {
      CriminogenicNeedDto.fromEntity(it).need
    },
    title = this.name,
    description = this.shortDescription,
    interventionType = this.interventionType,
    setting = deliveryMethodSettingList,
    allowsMales = this.personalEligibility?.males!!,
    allowsFemales = this.personalEligibility?.females!!,
    riskCriteria =
    this.riskConsideration?.let {
      RiskConsiderationDto.fromEntity(it).listOfRisks()
    },
    attendanceType = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.attendanceType },
    deliveryFormat = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.deliveryFormat },
    timeToComplete = this.timeToComplete,
    suitableForPeopleWithLearningDifficulties = this.specialEducationalNeeds?.learningDisabilityCateredFor,
    equivalentNonLdcProgramme = this.specialEducationalNeeds?.equivalentNonLdcProgrammeGuide,
  )
}
