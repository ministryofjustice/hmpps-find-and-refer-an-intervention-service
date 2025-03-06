package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.CrsInterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toCrsDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.toDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.toDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PduRefRepository
import java.util.UUID

@Service
class InterventionService(
  val interventionCatalogueRepository: InterventionCatalogueRepository,
  val pduRefRepository: PduRefRepository,
) {

  fun getInterventionsCatalogueByCriteria(
    pageable: Pageable,
    interventionTypes: List<InterventionType>?,
    settingType: SettingType?,
    allowsMales: Boolean?,
    allowsFemales: Boolean?,
    programmeName: String?,
  ): Page<InterventionCatalogueDto> = interventionCatalogueRepository
    .findAllInterventionCatalogueByCriteria(
      pageable,
      allowsFemales,
      allowsMales,
      interventionTypes,
      settingType,
      programmeName,
    )
    .map { it.toDto() }

  fun getInterventionDetailsById(interventionId: UUID): InterventionDetailsDto? = interventionCatalogueRepository
    .findInterventionCatalogueById(interventionId)?.toDetailsDto()

  fun getCrsInterventionDetailsByIdAndPdu(interventionId: UUID, pduRefId: String): CrsInterventionDetailsDto? {
    val interventionCatalogue =
      interventionCatalogueRepository.findInterventionCatalogueById(interventionId) ?: return null
    val pduRef = pduRefRepository.findPduRefById(pduRefId) ?: return null
    return interventionCatalogue.toCrsDetailsDto(pduRef)
  }
}
