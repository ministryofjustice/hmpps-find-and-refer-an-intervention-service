package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.OrganisationDto
import java.util.UUID

@Entity
@Table(name = "organisation", schema = "public")
open class Organisation(
  @NotNull
  @Id
  @Column(name = "organisation_id")
  open var id: UUID,

  @NotNull
  @Column(name = "code", length = Integer.MAX_VALUE)
  open var code: String,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @Nullable
  @Column(name = "gender", length = Integer.MAX_VALUE)
  open var gender: String? = null,

  @Nullable
  @Column(name = "category", length = Integer.MAX_VALUE)
  open var category: String? = null,

  @Nullable
  @Column(name = "county", length = Integer.MAX_VALUE)
  open var county: String? = null,
)

fun Organisation.toDto() = OrganisationDto(
  id = this.id,
  code = this.code,
  name = this.name,
  gender = this.gender,
  category = this.category,
  county = this.county,
)
