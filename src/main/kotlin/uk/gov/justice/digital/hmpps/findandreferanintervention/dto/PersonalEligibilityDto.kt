package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility}
 */
data class PersonalEligibilityDto(
  val minAge: Int? = null,
  val maxAge: Int? = null,
  @NotNull val males: Boolean? = false,
  @NotNull val females: Boolean? = false,
) {
  companion object {
    fun fromEntity(personalEligibility: PersonalEligibility): PersonalEligibilityDto {
      return PersonalEligibilityDto(
        minAge = personalEligibility.minAge,
        maxAge = personalEligibility.maxAge,
        males = personalEligibility.males,
        females = personalEligibility.females,
      )
    }
  }
}
