package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import java.util.UUID

// Excludes any properties that have null values when creating dto.
@JsonInclude(JsonInclude.Include.NON_NULL)
data class InterventionDetailsDto(
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
  val suitableForPeopleWithLearningDifficulties: Boolean?,
  val equivalentNonLdcProgramme: String?,
) {
  companion object {
    fun fromEntity(
      interventionCatalogue: InterventionCatalogue,
    ): InterventionDetailsDto {
      val deliveryMethodDtos =
        interventionCatalogue.deliveryMethods.map { DeliveryMethodDto.fromEntity(it) }
      val deliveryMethodSettingList =
        deliveryMethodDtos.flatMap { methodDto ->
          methodDto.deliveryMethodSettings.map { settingDto -> settingDto.setting }
        }
      return InterventionDetailsDto(
        id = interventionCatalogue.id,
        criminogenicNeeds =
        interventionCatalogue.criminogenicNeeds.map {
          CriminogenicNeedDto.fromEntity(it).need
        },
        title = interventionCatalogue.name,
        description = interventionCatalogue.shortDescription,
        interventionType = interventionCatalogue.interventionType,
        setting = deliveryMethodSettingList,
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
      )
    }
  }
}
