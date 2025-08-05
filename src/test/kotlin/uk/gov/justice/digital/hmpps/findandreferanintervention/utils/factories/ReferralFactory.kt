package uk.gov.justice.digital.hmpps.findandreferanintervention.utils.factories

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonReferenceType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Referral
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SourcedFromReferenceType
import java.util.UUID

class ReferralFactory(em: TestEntityManager? = null) : EntityFactory(em)

fun ReferralFactory.create(
  id: UUID = UUID.randomUUID(),
  settingType: SettingType = SettingType.COMMUNITY,
  interventionType: InterventionType = InterventionType.ACP,
  interventionName: String = "Horizon",
  personReference: String = "EXAMPLE_CRN",
  personReferenceType: PersonReferenceType = PersonReferenceType.CRN,
  sourcedFromReferenceType: SourcedFromReferenceType = SourcedFromReferenceType.REQUIREMENT,
  sourcedFromReference: String = "EXAMPLE_REFERENCE",
  eventNumber: Int = 1,
): Referral = save(
  Referral(
    id = id,
    settingType = settingType,
    interventionType = interventionType,
    interventionName = interventionName,
    personReference = personReference,
    personReferenceType = personReferenceType,
    sourcedFromReferenceType = sourcedFromReferenceType,
    sourcedFromReference = sourcedFromReference,
    eventNumber = eventNumber,
  ),
)
