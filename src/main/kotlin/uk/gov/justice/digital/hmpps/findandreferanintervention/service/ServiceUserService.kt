package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.ServiceUserDto
import java.time.LocalDate

@Service
class ServiceUserService {

  fun getServiceUserByCrn(crn: String): ServiceUserDto {
    // TODO Call delius client to retrieve the service user details for the crn. In the mean time return a stub response
    return ServiceUserDto(
      name = "John",
      crn = "X718255",
      dob = LocalDate.now(),
      gender = "Male",
      "British",
      currentPdu = "East Sussex",
      setting = "Community",
    )
  }

  fun getServiceUserByPrisonId(prisonId: String): ServiceUserDto? {
    // TODO Call delius client to retrieve the service user details for the crn. In the mean time return a stub response
    return ServiceUserDto(
      name = "John",
      crn = "X718255",
      dob = LocalDate.now(),
      gender = "Male",
      "British",
      currentPdu = "East Sussex",
      setting = "Custody",
    )
  }
}
