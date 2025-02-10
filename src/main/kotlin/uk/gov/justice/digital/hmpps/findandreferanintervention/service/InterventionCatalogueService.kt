package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository

@Service
class InterventionCatalogueService(
  val interventionCatalogueRepository: InterventionCatalogueRepository,
) {

  fun getInterventionsCatalogueByCriteria(
    pageable: Pageable,
    interventionTypes: List<InterventionType>?,
    settingType: SettingType?,
    allowsMales: Boolean?,
    allowsFemales: Boolean?,
  ): Page<InterventionCatalogueDto> = interventionCatalogueRepository
    .findAllInterventionCatalogueByCriteria(pageable, allowsFemales, allowsMales, interventionTypes, settingType)
    .map { intervention -> InterventionCatalogueDto.fromEntity(intervention) }
}
