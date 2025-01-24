package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import jakarta.validation.constraints.NotNull

data class CriminogenicNeedDto(@field:NotNull val need: CriminogenicNeedRefDto? = null)
