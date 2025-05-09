package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogueToCourseMap
import java.util.*

interface InterventionCatalogueToCourseMapRepository : JpaRepository<InterventionCatalogueToCourseMap, UUID> {
  fun deleteByInterventionCatalogueIdAndCourseId(interventionCatalogueId: UUID, courseId: UUID)
  fun findByInterventionCatalogueIdAndCourseId(interventionCatalogueId: UUID, courseId: UUID): InterventionCatalogueToCourseMap?
}
