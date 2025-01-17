package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeedRef}
 */
data class CriminogenicNeedRefDto(@field:NotNull val name: String? = null) : Serializable
