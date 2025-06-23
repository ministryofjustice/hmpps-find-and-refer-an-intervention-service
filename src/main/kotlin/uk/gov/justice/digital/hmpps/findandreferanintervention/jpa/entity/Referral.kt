package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Entity
@Table(name = "referral", schema = "public")
open class Referral(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @Column(name = "event_type", length = Integer.MAX_VALUE)
  open var eventType: String,

  @NotNull
  @Column(name = "main_type", length = Integer.MAX_VALUE)
  open var mainType: String,

  @NotNull
  @Column(name = "person_reference", length = Integer.MAX_VALUE)
  open var personReference: String,
)
