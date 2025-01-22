package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue

data class InterventionCatalogueDto(
  val title: String,
  val description: String,
  val intType: InterventionCatalogue.InterventionType,
  val setting: List<DeliveryMethodSettingDto>,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val minAge: Int,
  val maxAge: Int,
  val riskCriteria: List<String?>,
  val attendanceType: List<String>,
  val deliveryMethod: List<DeliveryMethodSettingDto>,
) {
  // TODO check all nullable conditions
  companion object {
    fun fromEntity(
      interventionCatalogue: InterventionCatalogue,
    ): InterventionCatalogueDto {
      val deliveryMethodDtos =
        interventionCatalogue.deliveryMethods.map { DeliveryMethodDto.fromEntity(it) }
      return InterventionCatalogueDto(
        title = interventionCatalogue.name!!,
        description = interventionCatalogue.shortDescription!!,
        intType = interventionCatalogue.intType!!,
        setting = deliveryMethodDtos.flatMap { it.deliveryMethodSettings },
        allowsMales = interventionCatalogue.personalEligibility?.males!!,
        allowsFemales = interventionCatalogue.personalEligibility?.females!!,
        minAge = interventionCatalogue.personalEligibility?.minAge!!,
        maxAge = interventionCatalogue.personalEligibility?.maxAge!!,
        riskCriteria = RiskConsiderationDto.fromEntity(interventionCatalogue.riskConsideration!!).listOfRisks(),
        attendanceType = deliveryMethodDtos.map { it.description },
        deliveryMethod = deliveryMethodDtos.flatMap { it.deliveryMethodSettings },
      )
    }
  }
}
