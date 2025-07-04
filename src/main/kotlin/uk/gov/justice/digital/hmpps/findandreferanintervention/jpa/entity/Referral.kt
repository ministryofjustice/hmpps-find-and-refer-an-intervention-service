package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import uk.gov.justice.digital.hmpps.findandreferanintervention.dto.ReferralDto
import java.util.UUID

@Entity
@Table(name = "referral", schema = "public")
open class Referral(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @Column(name = "setting")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var settingType: SettingType,

  @NotNull
  @Column(name = "intervention_type")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var interventionType: InterventionType,

  @NotNull
  @Column(name = "intervention_name", length = Integer.MAX_VALUE)
  open var interventionName: String,

  @NotNull
  @Column(name = "person_reference", length = Integer.MAX_VALUE)
  open var personReference: String,

  @NotNull
  @Column(name = "person_reference_type")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var personReferenceType: PersonReferenceType,

  @NotNull
  @Column(name = "sourced_from_reference_type")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var sourcedFromReferenceType: SourcedFromReferenceType,

  @NotNull
  @Column(name = "sourced_from_reference", length = Integer.MAX_VALUE)
  open var sourcedFromReference: String,

  @ManyToOne
  @JoinColumn(name = "office_id")
  open var office: Office? = null,
)

enum class PersonReferenceType {
  CRN,
  NOMS,
}

enum class SourcedFromReferenceType {
  LICENCE_CONDITION,
  REQUIREMENT,
}

fun Referral.toDto(): ReferralDto = ReferralDto(
  id = this.id,
  settingType = this.settingType,
  interventionType = this.interventionType,
  interventionName = this.interventionName,
  personReference = this.personReference,
  personReferenceType = this.personReferenceType,
  sourcedFromReferenceType = this.sourcedFromReferenceType,
  sourcedFromReference = this.sourcedFromReference,
  office = this.office?.toDto(),
)
