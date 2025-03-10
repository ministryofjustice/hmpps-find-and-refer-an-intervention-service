package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Intervention
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.getNpsRegion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.getPccRegionNames
import java.util.UUID

data class CrsInterventionDetailsDto(
  val interventionCatalogueId: UUID,
  val interventionId: UUID,
  val interventionType: InterventionType,
  val npsRegion: String,
  val pccRegions: List<String>,
  val serviceCategories: List<String>,
  val provider: String,
  val minAge: Int?,
  val maxAge: Int?,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val description: String,
)

fun InterventionCatalogue.toCrsDetailsDto(
  intervention: Intervention,
): CrsInterventionDetailsDto {
  val contract = intervention.dynamicFrameworkContract
  return CrsInterventionDetailsDto(
    interventionCatalogueId = id,
    interventionId = intervention.id,
    interventionType = interventionType,
    npsRegion = contract.getNpsRegion().name,
    pccRegions = contract.getPccRegionNames(),
    serviceCategories = contract.contractType.serviceCategories.map { it.name }.sorted(),
    provider = contract.primeProvider.name,
    minAge = contract.minAge,
    maxAge = contract.maxAge,
    allowsMales = contract.allowsMale,
    allowsFemales = contract.allowsFemale,
    description = intervention.description,
  )
}
