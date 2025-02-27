package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "intervention_catalogue_to_course_map", schema = "public")
open class InterventionCatalogueToCourseMap(
  @NotNull
  @Id
  open var id: UUID,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_catalogue_id")
  open var interventionCatalogue: InterventionCatalogue,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  open var course: Course,
)
