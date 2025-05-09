package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.io.Serializable
import java.util.UUID

@Entity
@IdClass(InterventionCatalogueMapId::class)
@Table(name = "intervention_catalogue_map", schema = "public")
open class InterventionCatalogueMap(

  @NotNull
  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "intervention_catalogue_id")
  open var interventionCatalogue: InterventionCatalogue,

  @NotNull
  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "intervention_id")
  open var intervention: Intervention,
)

data class InterventionCatalogueMapId(
  val interventionCatalogue: UUID = UUID.randomUUID(),
  val intervention: UUID = UUID.randomUUID(),
) : Serializable
