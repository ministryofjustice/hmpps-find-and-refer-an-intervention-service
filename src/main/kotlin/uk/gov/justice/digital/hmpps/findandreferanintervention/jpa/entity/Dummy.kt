package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "dummy_table")
class Dummy(
  @Id
  val dummyId: Int,
  val dummyDescription: String,
  val dummyDate: LocalDateTime,
)
