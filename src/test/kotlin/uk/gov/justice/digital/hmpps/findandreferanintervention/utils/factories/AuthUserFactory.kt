package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.AuthUser

class AuthUserFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun AuthUserFactory.create(
  id: String = "123456789",
  authSource: String = "Delius",
  userName: String = "Robert Mercury",
  deleted: Boolean = false,
): AuthUser = save(
  AuthUser(
    id,
    authSource,
    userName,
    deleted,
  ),
)
