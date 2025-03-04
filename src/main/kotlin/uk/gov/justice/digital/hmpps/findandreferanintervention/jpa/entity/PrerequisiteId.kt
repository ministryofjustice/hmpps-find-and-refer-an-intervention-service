package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Embeddable
open class PrerequisiteId(
  @NotNull
  @Column(name = "course_id")
  open var courseId: UUID,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @NotNull
  @Column(name = "description", length = Integer.MAX_VALUE)
  open var description: String,
)
