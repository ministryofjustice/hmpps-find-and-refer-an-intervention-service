package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
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
  ): Page<InterventionCatalogue> {
    val criteriaQuery: CriteriaQuery<InterventionCatalogue> =
      criteriaBuilder.createQuery(InterventionCatalogue::class.java)
    val root = criteriaQuery.from(InterventionCatalogue::class.java)

    allowsFemales?.let { filterByFemale(criteriaQuery, root, allowsFemales) }
    allowsMales?.let { filterByMale(criteriaQuery, root, allowsMales) }
    interventionTypes?.let { filterByInterventionType(criteriaQuery, root, interventionTypes) }
    settingType?.let { filterBySetting(criteriaQuery, root, settingType) }

    val query = entityManager.createQuery(criteriaQuery)
    query.setFirstResult(pageable.offset.toInt())
    query.setMaxResults(pageable.pageSize)

    val resultList = query.resultList

    val totalCount = getTotalCount(allowsFemales, allowsMales, interventionTypes, settingType)

    return PageImpl(resultList, pageable, totalCount)
  }

  private fun <T> filterByFemale(
    criteriaQuery: CriteriaQuery<T>,
    root: Root<InterventionCatalogue>,
    allowsFemales: Boolean?,
  ): CriteriaQuery<T> = criteriaQuery.where(
    criteriaBuilder.equal(
      root.get<PersonalEligibility>("personalEligibility").get<Boolean>("females"),
      allowsFemales,
    ),
  )

  private fun <T> filterByMale(
    criteriaQuery: CriteriaQuery<T>,
    root: Root<InterventionCatalogue>,
    allowsMales: Boolean,
  ): CriteriaQuery<T> = criteriaQuery.where(
    criteriaBuilder.equal(
      root.get<PersonalEligibility>("personalEligibility").get<Boolean>("males"),
      allowsMales,
    ),
  )

  private fun <T> filterByInterventionType(
    criteriaQuery: CriteriaQuery<T>,
    root: Root<InterventionCatalogue>,
    interventionTypes: List<InterventionType>?,
  ): CriteriaQuery<T> = criteriaQuery.where(root.get<String>("interventionType").`in`(interventionTypes))

  private fun <T> filterBySetting(
    criteriaQuery: CriteriaQuery<T>,
    root: Root<InterventionCatalogue>,
    settingType: SettingType,
  ) = criteriaQuery.where(
    criteriaBuilder.equal(
      root.get<PersonalEligibility>("deliveryMethods").get<DeliveryMethodSetting>("deliveryMethodSettings")
        .get<SettingType>("setting"),
      settingType,
    ),
  )

  private fun getTotalCount(
    allowsFemales: Boolean?,
    allowsMales: Boolean?,
    interventionTypes: List<InterventionType>?,
    settingType: SettingType?,
  ): Long {
    val countCriteriaQuery: CriteriaQuery<Long> = criteriaBuilder.createQuery(Long::class.java)
    val countRoot: Root<InterventionCatalogue> =
      countCriteriaQuery.from(InterventionCatalogue::class.java)

    allowsFemales?.let { filterByMale(countCriteriaQuery, countRoot, allowsFemales) }
    allowsMales?.let { filterByMale(countCriteriaQuery, countRoot, allowsMales) }
    interventionTypes?.let {
      filterByInterventionType(countCriteriaQuery, countRoot, interventionTypes)
    }
    settingType?.let { filterBySetting(countCriteriaQuery, countRoot, settingType) }

    countCriteriaQuery.select(criteriaBuilder.count(countRoot))
    return entityManager.createQuery(countCriteriaQuery).singleResult
  }
}
