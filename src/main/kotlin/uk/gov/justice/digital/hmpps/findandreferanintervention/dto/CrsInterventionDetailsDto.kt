package uk.gov.justice.digital.hmpps.findandreferanintervention.dto

import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PduRef
import java.util.UUID

data class CrsInterventionDetailsDto(
  val interventionCatalogueId: UUID,
  val interventionType: InterventionType,
  val npsRegion: String,
  val pccRegions: List<String>,
  val serviceCategory: List<String>,
  val provider: String,
  val minAge: Int?,
  val maxAge: Int?,
  val allowsMales: Boolean,
  val allowsFemales: Boolean,
  val description: String,
)

fun InterventionCatalogue.toCrsDetailsDto(pduRef: PduRef): CrsInterventionDetailsDto? {
  val contractIdsForIntervention = this.interventions.map { it.dynamicFrameworkContract.id }
  val npsRegion = pduRef.pccRegion.npsRegion
  val pccRegions = npsRegion.pccRegions.map { it.name }.sorted()
  val contractInPdu = npsRegion.dynamicFrameworkContracts.find { it.id in contractIdsForIntervention } ?: return null
  return CrsInterventionDetailsDto(
    interventionCatalogueId = this.id,
    interventionType = this.interventionType,
    npsRegion = npsRegion.name,
    pccRegions = pccRegions,
    serviceCategory = contractInPdu.contractType.serviceCategories.map { it.name }.sorted(),
    provider = contractInPdu.primeProvider.name,
    minAge = contractInPdu.minimumAge,
    maxAge = contractInPdu.maximumAge,
    allowsMales = contractInPdu.allowsMale,
    allowsFemales = contractInPdu.allowsFemale,
    description = this.shortDescription,
  )
}
