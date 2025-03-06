package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethodSetting
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType

class InterventionCatalogueRepositoryImpl(private val entityManager: EntityManager) : InterventionCatalogueFilterRepository {

  private val criteriaBuilder: CriteriaBuilder = entityManager.criteriaBuilder

  override fun findAllInterventionCatalogueByCriteria(
    pageable: Pageable,
    allowsFemales: Boolean?,
    allowsMales: Boolean?,
    interventionTypes: List<InterventionType>?,
    settingType: SettingType?,
    programmeName: String?,
  ): Page<InterventionCatalogue> {
    val criteriaQuery: CriteriaQuery<InterventionCatalogue> =
      criteriaBuilder.createQuery(InterventionCatalogue::class.java)
    val root = criteriaQuery.from(InterventionCatalogue::class.java)
    criteriaQuery.where(getPredicates(root, allowsFemales, allowsMales, interventionTypes, settingType, programmeName))

    val query = entityManager.createQuery(criteriaQuery)
    query.setFirstResult(pageable.offset.toInt())
    query.setMaxResults(pageable.pageSize)

    val resultList = query.resultList

    val totalCount = getTotalCount(allowsFemales, allowsMales, interventionTypes, settingType, programmeName)

    return PageImpl(resultList, pageable, totalCount)
  }

  private fun filterByFemalePredicate(
    root: Root<InterventionCatalogue>,
    allowsFemales: Boolean?,
  ): Predicate? = allowsFemales?.let {
    criteriaBuilder.equal(
      root.get<PersonalEligibility>("personalEligibility").get<Boolean>("females"),
      allowsFemales,
    )
  }

  private fun filterByMalePredicate(
    root: Root<InterventionCatalogue>,
    allowsMales: Boolean?,
  ): Predicate? = allowsMales?.let {
    criteriaBuilder.equal(
      root.get<PersonalEligibility>("personalEligibility").get<Boolean>("males"),
      allowsMales,
    )
  }

  private fun filterByInterventionTypePredicate(
    root: Root<InterventionCatalogue>,
    interventionTypes: List<InterventionType>?,
  ): Predicate? = interventionTypes?.let { root.get<String>("interventionType").`in`(interventionTypes) }

  private fun filterBySettingPredicate(
    root: Root<InterventionCatalogue>,
    settingType: SettingType?,
  ): Predicate? = settingType?.let {
    criteriaBuilder.equal(
      root.get<PersonalEligibility>("deliveryMethods").get<DeliveryMethodSetting>("deliveryMethodSettings")
        .get<SettingType>("setting"),
      settingType,
    )
  }

  private fun filterByProgrammeNamePredicate(
    root: Root<InterventionCatalogue>,
    programmeName: String?,
  ): Predicate? = programmeName?.let {
    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%$programmeName%".lowercase())
  }

  private fun getTotalCount(
    allowsFemales: Boolean?,
    allowsMales: Boolean?,
    interventionTypes: List<InterventionType>?,
    settingType: SettingType?,
    programmeName: String?,
  ): Long {
    val countCriteriaQuery: CriteriaQuery<Long> = criteriaBuilder.createQuery(Long::class.java)
    val countRoot: Root<InterventionCatalogue> =
      countCriteriaQuery.from(InterventionCatalogue::class.java)

    countCriteriaQuery.where(
      getPredicates(
        countRoot,
        allowsFemales,
        allowsMales,
        interventionTypes,
        settingType,
        programmeName,
      ),
    )

    countCriteriaQuery.select(criteriaBuilder.count(countRoot))
    return entityManager.createQuery(countCriteriaQuery).singleResult
  }

  private fun getPredicates(
    root: Root<InterventionCatalogue>,
    allowsFemales: Boolean?,
    allowsMales: Boolean?,
    interventionTypes: List<InterventionType>?,
    settingType: SettingType?,
    programmeName: String?,
  ): Predicate {
    val predicates = listOfNotNull(
      filterByFemalePredicate(root, allowsFemales),
      filterByMalePredicate(root, allowsMales),
      filterByInterventionTypePredicate(root, interventionTypes),
      filterBySettingPredicate(root, settingType),
      filterByProgrammeNamePredicate(root, programmeName),
    )

    return criteriaBuilder.and(*predicates.toTypedArray())
  }
}
