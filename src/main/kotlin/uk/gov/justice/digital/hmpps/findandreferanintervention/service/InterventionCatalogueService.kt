package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository

@Service
class InterventionCatalogueService(
  val interventionCatalogueRepository: InterventionCatalogueRepository,
) {

  fun getInterventionsCatalogueByCriteria(
    pageable: Pageable,
    interventionTypes: List<InterventionType>?,
  ): Page<InterventionCatalogueDto> = interventionCatalogueRepository
    .findAllInterventionCatalogueByCriteria(pageable, interventionTypes)
    .map { intervention -> InterventionCatalogueDto.fromEntity(intervention) }
}
