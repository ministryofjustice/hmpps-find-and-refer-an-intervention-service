package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.util.UUID

@Entity
@Table(name = "personal_eligibility", schema = "public")
open class PersonalEligibility {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @ColumnDefault("18")
  @Column(name = "min_age")
  open var minAge: Int? = null

  @ColumnDefault("120")
  @Column(name = "max_age")
  open var maxAge: Int? = null

  @NotNull
  @Column(name = "males", nullable = false)
  open var males: Boolean? = false

  @NotNull
  @Column(name = "females", nullable = false)
  open var females: Boolean? = false

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id")
  open var intervention: InterventionCatalogue? = null
}
