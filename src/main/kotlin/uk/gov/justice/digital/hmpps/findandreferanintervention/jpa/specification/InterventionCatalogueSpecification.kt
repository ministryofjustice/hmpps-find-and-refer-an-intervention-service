package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.specification

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethodSetting
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType

fun getInterventionCatalogueSpecification(
  interventionTypes: List<InterventionType>? = null,
  settingType: SettingType? = null,
  allowsMales: Boolean? = null,
  allowsFemales: Boolean? = null,
  programmeName: String? = null,
): Specification<InterventionCatalogue> = Specification<InterventionCatalogue> { root: Root<InterventionCatalogue?>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
  val predicates: MutableList<Predicate> = mutableListOf()

  allowsMales?.let {
    predicates.add(
      criteriaBuilder.equal(
        root.get<PersonalEligibility>("personalEligibility").get<Boolean>("males"),
        allowsMales,
      ),
    )
  }
  allowsFemales?.let {
    predicates.add(
      criteriaBuilder.equal(
        root.get<PersonalEligibility>("personalEligibility").get<Boolean>("females"),
        allowsFemales,
      ),
    )
  }

  interventionTypes?.let {
    predicates.add(
      root.get<String>("interventionType").`in`(interventionTypes),
    )
  }

  settingType?.let {
    predicates.add(
      root.get<DeliveryMethodSetting>("deliveryMethodSettings").get<SettingType>("setting").`in`(settingType),
    )
  }
  programmeName?.let {
    predicates.add(
      criteriaBuilder.like(
        criteriaBuilder.lower(root.get("name")),
        "%$programmeName%".lowercase(),
      ),
    )
  }
  query?.distinct(true)
  criteriaBuilder.and(*predicates.toTypedArray())
}
