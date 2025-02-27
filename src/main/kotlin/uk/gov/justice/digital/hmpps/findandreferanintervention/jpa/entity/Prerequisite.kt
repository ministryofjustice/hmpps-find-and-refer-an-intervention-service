package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Entity
@Table(name = "prerequisite", schema = "public")
open class Prerequisite(
  @EmbeddedId
  open var id: PrerequisiteId? = null,

  @MapsId("courseId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  open var course: Course? = null,
)
