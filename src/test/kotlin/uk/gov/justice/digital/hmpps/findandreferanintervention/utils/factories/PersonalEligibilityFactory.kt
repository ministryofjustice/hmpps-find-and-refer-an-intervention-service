package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility
import java.util.UUID

class PersonalEligibilityFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun PersonalEligibilityFactory.create(
  id: UUID = UUID.randomUUID(),
  minAge: Int? = null,
  maxAge: Int? = null,
  males: Boolean = true,
  females: Boolean = false,
  intervention: InterventionCatalogue? = null,
): PersonalEligibility = save(
  PersonalEligibility(
    id,
    minAge,
    maxAge,
    males,
    females,
    intervention,
  ),
)
