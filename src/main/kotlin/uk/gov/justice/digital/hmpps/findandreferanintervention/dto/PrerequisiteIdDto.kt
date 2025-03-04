package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.util.UUID

data class PrerequisiteIdDto(val courseId: UUID, val name: String, val description: String)
