package uk.gov.justice.digital.hmpps.findandreferanintervention.event

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonReferenceType
import java.time.ZonedDateTime

data class HmppsDomainEvent(
  val eventType: String,
  val version: Int,
  val detailUrl: String? = null,
  val occurredAt: ZonedDateTime,
  val description: String? = null,
  val additionalInformation: Map<String, Any?>,
  val personReference: PersonReference,
)

data class PersonReference(val identifiers: List<Identifier> = listOf()) {
  fun findCrn() = get("CRN")
  fun findNomsNumber() = get(NOMS_NUMBER_TYPE)

  operator fun get(key: String) = identifiers.find { it.type == key }?.value

  companion object {
    const val NOMS_NUMBER_TYPE = "NOMS"
    fun withIdentifier(personReference: PersonReference): PersonReference {
      val (personReferenceType, personReferenceString) = personReference.getPersonReferenceTypeAndValue()
      return PersonReference(listOf(Identifier(personReferenceType.name, personReferenceString!!)))
    }
  }

  data class Identifier(val type: String, val value: String)
}

fun PersonReference.getPersonReferenceTypeAndValue(): Pair<PersonReferenceType, String?> = if (findCrn() != null) {
  Pair(PersonReferenceType.CRN, findCrn())
} else {
  Pair(
    PersonReferenceType.NOMS,
    findNomsNumber(),
  )
}
