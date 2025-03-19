package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface DeliveryLocationRepository : JpaRepository<DeliveryLocation, UUID> {
  fun findByIntervention(intervention: InterventionCatalogue): List<DeliveryLocation>?
}
