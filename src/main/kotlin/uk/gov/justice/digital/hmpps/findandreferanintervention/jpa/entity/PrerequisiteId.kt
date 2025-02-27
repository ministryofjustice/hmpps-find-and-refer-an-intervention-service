package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import org.hibernate.Hibernate
import java.util.Objects
import java.util.UUID

@Embeddable
open class PrerequisiteId {
  @NotNull
  @Column(name = "course_id", nullable = false)
  open var courseId: UUID? = null

  @NotNull
  @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
  open var name: String? = null

  @NotNull
  @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
  open var description: String? = null
  override fun hashCode(): Int = Objects.hash(courseId, name, description)
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

    other as PrerequisiteId

    return courseId == other.courseId &&
      name == other.name &&
      description == other.description
  }

  companion object {
    private const val serialVersionUID = -441203922704678861L
  }
}
