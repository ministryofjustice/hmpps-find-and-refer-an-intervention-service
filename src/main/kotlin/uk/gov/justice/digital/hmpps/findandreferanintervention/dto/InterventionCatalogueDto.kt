package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import java.util.UUID

data class InterventionCatalogueDto(
  val id: UUID,
  val title: String,
  val description: String,
  val intType: InterventionType,
  val setting: List<SettingType>,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val minAge: Int,
  val maxAge: Int,
  val riskCriteria: List<String?>?,
  val attendanceType: List<String>,
  val deliveryMethod: List<String>,
) {
  companion object {
    fun fromEntity(
      interventionCatalogue: InterventionCatalogue,
    ): InterventionCatalogueDto {
      val deliveryMethodDtos =
        interventionCatalogue.deliveryMethods.map { DeliveryMethodDto.fromEntity(it) }
      val deliveryMethodSettingList =
        deliveryMethodDtos.flatMap { methodDto -> methodDto.deliveryMethodSettings.map { settingDto -> settingDto.setting } }
      val riskCriteria = interventionCatalogue.riskConsideration
      return InterventionCatalogueDto(
        id = interventionCatalogue.id,
        title = interventionCatalogue.name,
        description = interventionCatalogue.shortDescription,
        intType = interventionCatalogue.interventionType,
        setting = deliveryMethodSettingList,
        allowsMales = interventionCatalogue.personalEligibility?.males!!,
        allowsFemales = interventionCatalogue.personalEligibility?.females!!,
        minAge = interventionCatalogue.personalEligibility?.minAge ?: 18,
        maxAge = interventionCatalogue.personalEligibility?.maxAge ?: 120,
        riskCriteria = interventionCatalogue.riskConsideration?.let {
          RiskConsiderationDto.fromEntity(it).listOfRisks()
        },
        // TODO need to add field in database for this field
        attendanceType = emptyList(),
        deliveryMethod = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.description },
      )
    }
  }
}
