package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * DTO for {@link uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeed}
 */
data class CriminogenicNeedDto(@field:NotNull val need: CriminogenicNeedRefDto? = null) : Serializable