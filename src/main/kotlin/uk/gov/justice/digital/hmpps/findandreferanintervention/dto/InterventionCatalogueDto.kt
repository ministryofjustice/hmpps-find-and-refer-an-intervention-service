package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import java.util.UUID

data class InterventionCatalogueDto(
  val id: UUID,
  val criminogenicNeeds: List<String>,
  val title: String,
  val description: String,
  val interventionType: InterventionType,
  val setting: List<SettingType>,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val minAge: Int?,
  val maxAge: Int?,
  val riskCriteria: List<String?>?,
  val attendanceType: List<String>,
  val deliveryFormat: List<String>,
) {
  companion object {
    fun fromEntity(
      interventionCatalogue: InterventionCatalogue,
    ): InterventionCatalogueDto {
      val deliveryMethodDtos =
        interventionCatalogue.deliveryMethods.map { DeliveryMethodDto.fromEntity(it) }
      val deliveryMethodSettingList =
        deliveryMethodDtos.flatMap { methodDto ->
          methodDto.deliveryMethodSettings.map { settingDto -> settingDto.setting }
        }
      return InterventionCatalogueDto(
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
        minAge = interventionCatalogue.personalEligibility?.minAge,
        maxAge = interventionCatalogue.personalEligibility?.maxAge,
        riskCriteria =
        interventionCatalogue.riskConsideration?.let {
          RiskConsiderationDto.fromEntity(it).listOfRisks()
        },
        attendanceType = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.attendanceType },
        deliveryFormat = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.deliveryFormat },
      )
    }
  }
}
