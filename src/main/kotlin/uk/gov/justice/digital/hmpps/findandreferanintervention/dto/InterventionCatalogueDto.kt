package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import arrow.core.raise.nullable
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

data class InterventionCatalogueDto(
  val id: UUID,
  val title: String,
  val description: String,
  val intType: InterventionCatalogue.InterventionType,
  val setting: List<DeliveryMethodSettingDto>,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val minAge: Int,
  val maxAge: Int,
  val riskCriteria: List<String?>,
  val reasonsForReferral: String,
  val attendanceType: List<String>,
  val deliveryMethod: List<DeliveryMethodSettingDto>,
) {
  companion object {
    fun fromEntity(
      interventionCatalogue: InterventionCatalogue,
    ): InterventionCatalogueDto? = nullable {
      val deliveryMethodDtos =
        interventionCatalogue.deliveryMethods.map { DeliveryMethodDto.fromEntity(it) }
      InterventionCatalogueDto(
        id = ensureNotNull(interventionCatalogue.id),
        title = ensureNotNull(interventionCatalogue.name),
        description = ensureNotNull(interventionCatalogue.shortDescription),
        intType = ensureNotNull(interventionCatalogue.intType),
        setting = deliveryMethodDtos.flatMap { it.deliveryMethodSettings },
        allowsMales = ensureNotNull(interventionCatalogue.personalEligibility?.males),
        allowsFemales = ensureNotNull(interventionCatalogue.personalEligibility?.females),
        minAge = ensureNotNull(interventionCatalogue.personalEligibility?.minAge),
        maxAge = ensureNotNull(interventionCatalogue.personalEligibility?.maxAge),
        riskCriteria = RiskConsiderationDto.fromEntity(interventionCatalogue.riskConsideration!!).listOfRisks(),
        reasonsForReferral = ensureNotNull(interventionCatalogue.reasonsForReferral),
        attendanceType = deliveryMethodDtos.map { ensureNotNull(it.description) },
        deliveryMethod = deliveryMethodDtos.flatMap { it.deliveryMethodSettings },
      )
    }
  }
}
