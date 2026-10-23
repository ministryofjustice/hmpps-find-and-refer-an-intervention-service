package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SpecialEducationalNeed
import java.util.UUID

class SpecialEducationNeedFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun SpecialEducationNeedFactory.create(
  id: UUID = UUID.randomUUID(),
  literacyLevelGuide: String? = null,
  learningDisabilityCateredFor: String? = null,
  equivalentNonLdcProgrammeGuide: String? = null,
  intervention: InterventionCatalogue? = null,
): SpecialEducationalNeed = save(
  SpecialEducationalNeed(
    id,
    literacyLevelGuide,
    learningDisabilityCateredFor,
    equivalentNonLdcProgrammeGuide,
    intervention,
  ),
)
