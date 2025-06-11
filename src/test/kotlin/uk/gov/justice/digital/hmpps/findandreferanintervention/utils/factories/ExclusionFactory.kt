package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Exclusion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

class ExclusionFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun ExclusionFactory.create(
  id: UUID = UUID.randomUUID(),
  minRemainingSentenceDurationGuide: String? = null,
  remainingLicenseCommunityOrderGuide: String? = null,
  alcoholDrugProblemGuide: String? = null,
  mentalHealthProblemGuide: String? = null,
  otherPreferredMethodGuide: String? = null,
  sameTypeRuleGuide: String? = null,
  notAllowedIfEligibleForAnotherInterventionGuide: String? = null,
  literacyLevelGuide: String? = null,
  scheduleFrequencyGuide: String? = null,
  convictedForOffenceTypeGuide: String? = null,
  intervention: InterventionCatalogue? = null,
): Exclusion = save(
  Exclusion(
    id,
    minRemainingSentenceDurationGuide,
    remainingLicenseCommunityOrderGuide,
    alcoholDrugProblemGuide,
    mentalHealthProblemGuide,
    otherPreferredMethodGuide,
    sameTypeRuleGuide,
    scheduleFrequencyGuide,
    notAllowedIfEligibleForAnotherInterventionGuide,
    literacyLevelGuide,
    convictedForOffenceTypeGuide,
    intervention,
  ),
)
