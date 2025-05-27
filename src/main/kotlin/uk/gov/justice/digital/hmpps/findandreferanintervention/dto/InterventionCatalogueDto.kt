package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import java.util.UUID

@Schema(
  description = "Intervention Catalogue DTO, contains information about a specific Intervention.",
)
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes any properties that have null values when creating dto.
data class InterventionCatalogueDto(
  @field:Schema(description = "Unique Identifier (UUID)", type = "UUID")
  val id: UUID,

  @field:Schema(
    description = "A list of the names of associated Criminogenic Needs",
    type = "List<String>",
    example = "[\"Employment and Education\", \"Relatives and Family\"]",
  )
  val criminogenicNeeds: List<String>,

  @field:Schema(
    description = "Name of the Intervention Catalogue entry",
    type = "String",
    example = "Accommodation",
  )
  val title: String,

  @field:Schema(
    description = "A Short Description of the Intervention.  Sourced from the shortDescription field in the database.",
    type = "String",
    example = "provides male individuals on a community/suspended sentence order with a RAR or on license/post-sentence supervision with non-CAS accommodation interventions.",
  )
  val description: String,

  @field:Schema(
    description = "The type of Intervention (abbreviated)",
    type = "String",
    allowableValues = ["SI", "ACP", "CRS", "TOOLKITS"],
  )
  val interventionType: InterventionType,

  @field:Schema(
    description = "The Settings that the Intervention Catalogue Entry is permitted in",
    allowableValues = ["COMMUNITY", "CUSTODY", "REMAND", "PRE_RELEASE"],
    type = "List<String>",
  )
  val setting: List<SettingType>,

  val allowsMales: Boolean,

  val allowsFemales: Boolean,

  val riskCriteria: List<String>?,

  @field:Schema(
    description = "List of the Attendance Types (empty if null)",
    example = "[\"In Person\"]",
    type = "List<String>",
    allowableValues = ["In Person"],
    nullable = false,
  )
  val attendanceType: List<String>,

  @field:Schema(
    description = "List of Delivery Formats",
    example = "[\"One-to-One\"]",
    type = "List<String>",
    allowableValues = ["One-to-One", "Group"],
  )
  val deliveryFormat: List<String>,

  @field:Schema(
    description = "A human-readable description of the estimated time to complete the intervention.  Likely null (and therefore missing)",
    nullable = true,
    type = "String?",
  )
  val timeToComplete: String?,

  @field:Schema(
    description = "A human-readable description of the Learning Difficulties this Intervention caters for, nullable",
    example = "Assessed case by case",
    type = "String?",
    nullable = true,
  )
  val suitableForPeopleWithLearningDifficulties: String?,

  @field:Schema(
    description = "Name of an equivalent Programme without Learning Difficulties and Challenges (LDC).  Name is entered manually so may not be an exact match.  Often null.",
    nullable = true,
    example = "Becoming New Me Plus",
  )
  val equivalentNonLdcProgramme: String?,

  @field:Schema(
    description = "The minimum age that is required for the Intervention",
    nullable = true,
    example = "18",

  )
  val minAge: Int?,
  @field:Schema(
    description = "The maximum age that is required for the Intervention",
    nullable = true,
    example = "18",

  )
  val maxAge: Int?,
)

fun InterventionCatalogue.toDto(): InterventionCatalogueDto {
  val deliveryMethodDtos =
    this.deliveryMethods.map { DeliveryMethodDto.fromEntity(it) }
  val settingList =
    this.deliveryMethodSettings.map { DeliveryMethodSettingDto.fromEntity(it).setting }

  return InterventionCatalogueDto(
    id = this.id,
    criminogenicNeeds =
    this.criminogenicNeeds.map {
      CriminogenicNeedDto.fromEntity(it).need
    }.sorted(),
    title = this.name,
    description = this.shortDescription,
    interventionType = this.interventionType,
    setting = settingList,
    allowsMales = this.personalEligibility?.males!!,
    allowsFemales = this.personalEligibility?.females!!,
    riskCriteria =
    this.riskConsideration?.let {
      RiskConsiderationDto.fromEntity(it).listOfRisks()
    },
    attendanceType = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.attendanceType }.sorted(),
    deliveryFormat = deliveryMethodDtos.mapNotNull { methodDto -> methodDto.deliveryFormat }.sorted(),
    timeToComplete = this.timeToComplete,
    suitableForPeopleWithLearningDifficulties = this.specialEducationalNeeds?.learningDisabilityCateredFor,
    equivalentNonLdcProgramme = this.specialEducationalNeeds?.equivalentNonLdcProgrammeGuide,
    minAge = this.personalEligibility?.minAge,
    maxAge = this.personalEligibility?.maxAge,
  )
}
