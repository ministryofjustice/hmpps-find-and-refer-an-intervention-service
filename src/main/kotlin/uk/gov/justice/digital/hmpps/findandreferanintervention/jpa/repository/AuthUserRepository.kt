package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.AuthUser

interface AuthUserRepository : CrudRepository<AuthUser, String> {
  fun findByUserName(userName: String?): AuthUser?
}
