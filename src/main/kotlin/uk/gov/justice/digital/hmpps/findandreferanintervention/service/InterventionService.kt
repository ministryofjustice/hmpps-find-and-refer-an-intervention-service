package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.CrsInterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toCrsDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.toDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.getPduRefsForContract
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.toDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import java.util.UUID

@Service
class InterventionService(
  val interventionCatalogueRepository: InterventionCatalogueRepository,
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

  fun getInterventionDetailsById(interventionCatalogueId: UUID): InterventionDetailsDto? = interventionCatalogueRepository
    .findInterventionCatalogueById(interventionCatalogueId)?.toDetailsDto()

  fun getCrsInterventionDetailsByIdAndPdu(interventionCatalogueId: UUID, pduRefId: String): CrsInterventionDetailsDto? {
    val interventionCatalogue =
      interventionCatalogueRepository.findInterventionCatalogueById(interventionCatalogueId) ?: return null
    val contractsToPccRegion =
      interventionCatalogue.interventions.map { intervention ->
        intervention.dynamicFrameworkContract to intervention.dynamicFrameworkContract.getPduRefsForContract()
          .map { it.id }
      }.firstOrNull { pduRef -> pduRef.second.contains(pduRefId) } ?: return null

    val (contract) = contractsToPccRegion
    val interventionForContract =
      interventionCatalogue.interventions.find { it.dynamicFrameworkContract.id == contract.id } ?: return null
    return interventionCatalogue.toCrsDetailsDto(interventionForContract)
  }
}
