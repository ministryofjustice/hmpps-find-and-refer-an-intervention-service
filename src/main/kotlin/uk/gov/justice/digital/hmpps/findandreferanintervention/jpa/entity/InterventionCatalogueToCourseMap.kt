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
@IdClass(InterventionCatalogueToCourseMapId::class)
@Table(name = "intervention_catalogue_to_course_map", schema = "public")
open class InterventionCatalogueToCourseMap(

  @NotNull
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_catalogue_id")
  open var interventionCatalogue: InterventionCatalogue,

  @NotNull
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  open var course: Course,
)

data class InterventionCatalogueToCourseMapId(
  var interventionCatalogue: UUID? = null,
  var course: UUID? = null,
) : Serializable
