package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
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
