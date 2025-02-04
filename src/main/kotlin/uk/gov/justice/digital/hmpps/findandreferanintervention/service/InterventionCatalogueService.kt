package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository

@Service
class InterventionCatalogueService(
  val interventionCatalogueRepository: InterventionCatalogueRepository,
) {

  fun getInterventionsCatalogue(pageable: Pageable): Page<InterventionCatalogueDto> = interventionCatalogueRepository.findAll(pageable)
    .map { intervention -> InterventionCatalogueDto.fromEntity(intervention) }
}
