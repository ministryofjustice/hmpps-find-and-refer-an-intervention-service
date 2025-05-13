package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethod
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import java.util.UUID

interface DeliveryMethodRepository : JpaRepository<DeliveryMethod, UUID> {
  fun findByIntervention(intervention: InterventionCatalogue): List<DeliveryMethod>?
  fun deleteAllByIntervention(intervention: InterventionCatalogue)
}
