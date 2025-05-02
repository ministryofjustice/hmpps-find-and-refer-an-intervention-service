package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import java.time.LocalDate

data class ServiceUserDto(
  var name: String? = null,
  var crn: String? = null,
  var dob: LocalDate,
  var gender: String? = null,
  var ethnicity: String? = null,
  var currentPdu: String? = null,
)
