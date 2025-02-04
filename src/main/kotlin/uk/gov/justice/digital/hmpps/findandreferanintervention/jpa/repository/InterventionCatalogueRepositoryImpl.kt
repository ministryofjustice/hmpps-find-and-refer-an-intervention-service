package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType

class InterventionCatalogueRepositoryImpl(private val entityManager: EntityManager) : InterventionCatalogueFilterRepository {

  private val criteriaBuilder: CriteriaBuilder = entityManager.criteriaBuilder

  override fun findAllInterventionCatalogueByCriteria(
    pageable: Pageable,
    interventionType: InterventionType?,
  ): Page<InterventionCatalogue> {
    val criteriaQuery: CriteriaQuery<InterventionCatalogue> =
      criteriaBuilder.createQuery(InterventionCatalogue::class.java)
    val root: Root<InterventionCatalogue> = criteriaQuery.from(InterventionCatalogue::class.java)

    interventionType?.let { filterByInterventionType(interventionType, criteriaQuery, root) }

    val query = entityManager.createQuery(criteriaQuery)
    query.setFirstResult(pageable.offset.toInt())
    query.setMaxResults(pageable.pageSize)

    val resultList = query.resultList

    val totalCount = getTotalCount(interventionType)

    return PageImpl(resultList, pageable, totalCount)
  }

  private fun <T> filterByInterventionType(
    interventionType: InterventionType?,
    criteriaQuery: CriteriaQuery<T>,
    root: Root<InterventionCatalogue>,
  ): CriteriaQuery<T> = criteriaQuery.where(
    criteriaBuilder.equal(root.get<String>("interventionType"), interventionType),
  )

  private fun getTotalCount(interventionType: InterventionType?): Long {
    val countCriteriaQuery: CriteriaQuery<Long> = criteriaBuilder.createQuery(Long::class.java)
    val countRoot: Root<InterventionCatalogue> =
      countCriteriaQuery.from(InterventionCatalogue::class.java)

    interventionType?.let {
      filterByInterventionType(interventionType, countCriteriaQuery, countRoot)
    }

    countCriteriaQuery.select(criteriaBuilder.count(countRoot))
    return entityManager.createQuery(countCriteriaQuery).singleResult
  }
}
