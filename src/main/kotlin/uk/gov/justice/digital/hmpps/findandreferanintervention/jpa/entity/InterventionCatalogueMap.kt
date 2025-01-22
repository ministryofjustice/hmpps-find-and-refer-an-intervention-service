package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "intervention_catalogue_map", schema = "public")
open class InterventionCatalogueMap {
  @Id
  open var id: UUID? = null
}
