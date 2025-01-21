package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility}
 */
data class PersonalEligibilityDto(
  val minAge: Int? = null,
  val maxAge: Int? = null,
  @field:NotNull val males: Boolean? = false,
  @field:NotNull val females: Boolean? = false,
) : Serializable