package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility
import java.util.UUID

data class PersonalEligibilityDto(
  val id: UUID,
  val minAge: Int?,
  val maxAge: Int?,
  val males: Boolean,
  val females: Boolean,
) {
  companion object {
    fun fromEntity(personalEligibility: PersonalEligibility): PersonalEligibilityDto {
      return PersonalEligibilityDto(
        id = personalEligibility.id,
        minAge = personalEligibility.minAge,
        maxAge = personalEligibility.maxAge,
        males = personalEligibility.males,
        females = personalEligibility.females,
      )
    }
  }
}
