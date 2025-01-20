package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "intervention_catalogue_map", schema = "public")
open class InterventionCatalogueMap {
  @Id
  open var id: UUID? = null

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "intervention_catalogue_id", nullable = false)
  open var interventionCatalogue: InterventionCatalogue? = null

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "intervention_id", nullable = false)
  open var intervention: Intervention? = null
}
