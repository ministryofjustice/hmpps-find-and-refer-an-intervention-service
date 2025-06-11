package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EnablingIntervention
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

class EnablingInterventionFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun EnablingInterventionFactory.create(
  id: UUID = UUID.randomUUID(),
  enablingInterventionDetail: String? = null,
  convictedForOffenceTypeGuide: String? = null,
  intervention: InterventionCatalogue? = null,
): EnablingIntervention = save(
  EnablingIntervention(
    id = id,
    enablingInterventionDetail = enablingInterventionDetail,
    convictedForOffenceTypeGuide = convictedForOffenceTypeGuide,
    intervention = intervention,
  ),
)
