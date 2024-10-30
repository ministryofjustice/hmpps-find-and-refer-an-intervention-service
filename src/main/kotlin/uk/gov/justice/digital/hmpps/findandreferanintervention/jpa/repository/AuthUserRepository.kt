package uk.gov.justice.digital.hmpps.hmppsfindandreferaninterventionsservice.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsfindandreferaninterventionsservice.jpa.entity.AuthUser

interface AuthUserRepository : CrudRepository<AuthUser, String> {
  fun findByUserName(userName: String?): AuthUser?
}
