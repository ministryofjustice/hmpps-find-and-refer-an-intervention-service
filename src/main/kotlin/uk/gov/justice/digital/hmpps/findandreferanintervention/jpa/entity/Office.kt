package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.OfficeDto
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "office")
class Office(

  @Id
  @Column(name = "id", nullable = false, unique = true)
  var id: UUID,

  @OneToOne
  @JoinColumn(name = "referral_id", referencedColumnName = "id", unique = true)
  var referral: Referral,

  @Column(name = "office_name", nullable = false)
  var officeName: String,

  @Column(name = "contact_email", nullable = false)
  var contactEmail: String,

  @Column(name = "created_at", nullable = false)
  var createdAt: OffsetDateTime,

  @Column(name = "created_by_user", nullable = false)
  var createdByUser: String,

  @Column(name = "deleted_at")
  var deletedAt: String? = null,
)

fun Office.toDto(): OfficeDto = OfficeDto(
  id = this.id,
  referral = this.referral.toDto(),
  officeName = this.officeName,
  contactEmail = this.contactEmail,
  createdAt = this.createdAt.toString(),
  createdByUser = this.createdByUser,
  deletedAt = this.deletedAt,
)
