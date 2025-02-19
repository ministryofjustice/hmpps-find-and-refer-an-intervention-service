package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionRepository
import java.util.UUID

@Service
class InterventionService(
  val interventionRepository: InterventionRepository,
) {

  fun getInterventionsCatalogueByCriteria(
    pageable: Pageable,
    interventionTypes: List<InterventionType>?,
    settingType: SettingType?,
    allowsMales: Boolean?,
    allowsFemales: Boolean?,
  ): Page<InterventionCatalogueDto> = interventionRepository
    .findAllInterventionCatalogueByCriteria(pageable, allowsFemales, allowsMales, interventionTypes, settingType)
    .map { intervention -> InterventionCatalogueDto.fromEntity(intervention) }

  fun getInterventionDetailsById(
    interventionId: UUID,
  ): InterventionDetailsDto = InterventionDetailsDto.fromEntity(interventionRepository.findInterventionCatalogueById(interventionId))
}
