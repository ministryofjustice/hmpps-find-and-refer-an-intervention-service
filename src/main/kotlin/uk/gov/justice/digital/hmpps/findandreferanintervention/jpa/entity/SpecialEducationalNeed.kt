package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.util.UUID

@Entity
@Table(name = "special_educational_need", schema = "public")
open class SpecialEducationalNeed {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "intervention_id", nullable = false)
  open var intervention: InterventionCatalogue? = null

  @Column(name = "literacy_level_guide", length = Integer.MAX_VALUE)
  open var literacyLevelGuide: String? = null

  @NotNull
  @ColumnDefault("false")
  @Column(name = "learning_disability_catered_for", nullable = false)
  open var learningDisabilityCateredFor: Boolean? = false

  @Column(name = "equivalent_non_ldc_programme_guide", length = Integer.MAX_VALUE)
  open var equivalentNonLdcProgrammeGuide: String? = null
}
