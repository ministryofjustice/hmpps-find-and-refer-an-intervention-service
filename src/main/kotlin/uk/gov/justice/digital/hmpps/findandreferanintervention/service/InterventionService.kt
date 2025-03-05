package uk.gov.justice.digital.hmpps.findandreferanintervention.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.CrsInterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionCatalogueDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.InterventionDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.toDetailsDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.toDto
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import java.util.UUID

@Service
class InterventionService(
  val interventionRepository: InterventionCatalogueRepository,
) {

  fun getInterventionsCatalogueByCriteria(
    pageable: Pageable,
    interventionTypes: List<InterventionType>?,
    settingType: SettingType?,
    allowsMales: Boolean?,
    allowsFemales: Boolean?,
    programmeName: String?,
  ): Page<InterventionCatalogueDto> = interventionRepository
    .findAllInterventionCatalogueByCriteria(pageable, allowsFemales, allowsMales, interventionTypes, settingType, programmeName)
    .map { it.toDto() }

  fun getInterventionDetailsById(interventionId: UUID): InterventionDetailsDto? = interventionRepository
    .findInterventionCatalogueById(interventionId)?.toDetailsDto()

  fun getCrsInterventionDetailsByIdAndPdu(interventionId: UUID, pduRefId: String): CrsInterventionDetailsDto? {
    val intervention = interventionRepository.findInterventionCatalogueById(interventionId) ?: return null
    return CrsInterventionDetailsDto(
      interventionCatalogueId = interventionId,
      interventionType = intervention.interventionType,
      region = "RegionTest",
      location = "LocationTest",
      serviceCategory = "TODO()",
      provider = "TODO()",
      minAge = intervention.personalEligibility?.minAge,
      maxAge = intervention.personalEligibility?.maxAge,
      allowsMales = intervention.personalEligibility!!.males,
      allowsFemales = intervention.personalEligibility!!.females,
      description = intervention.shortDescription,
    )
  }
}
