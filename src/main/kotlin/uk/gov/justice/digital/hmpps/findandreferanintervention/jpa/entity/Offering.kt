package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.OfferingDto
import java.util.UUID

@Entity
@Table(name = "offering", schema = "public")
open class Offering(
  @NotNull
  @Id
  @Column(name = "offering_id")
  open var id: UUID,

  @NotNull
  @Column(name = "organisation_id", length = Integer.MAX_VALUE)
  open var organisationId: String,

  @NotNull
  @Column(name = "contact_email", length = Integer.MAX_VALUE)
  open var contactEmail: String,

  @Nullable
  @Column(name = "secondary_contact_email", length = Integer.MAX_VALUE)
  open var secondaryContactEmail: String? = null,

  @NotNull
  @ColumnDefault("false")
  @Column(name = "withdrawn")
  open var withdrawn: Boolean = false,

  @NotNull
  @ColumnDefault("true")
  @Column(name = "referable")
  open var referable: Boolean = true,

  @NotNull
  @ColumnDefault("0")
  @Column(name = "version")
  open var version: Long = 0,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  open var course: Course,
)

fun Offering.toDto(): OfferingDto = OfferingDto(
  id = this.id,
  organisationId = this.organisationId,
  contactEmail = this.contactEmail,
  secondaryContactEmail = this.secondaryContactEmail,
  withdrawn = this.withdrawn,
  referable = this.referable,
  version = this.version,
)
