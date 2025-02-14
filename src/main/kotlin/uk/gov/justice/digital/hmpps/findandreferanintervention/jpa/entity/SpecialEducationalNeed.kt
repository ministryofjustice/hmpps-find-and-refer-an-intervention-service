package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
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
@Table(name = "special_educational_need", schema = "public")
open class SpecialEducationalNeed(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @Column(name = "literacy_level_guide", length = Integer.MAX_VALUE)
  open var literacyLevelGuide: String? = null,

  @NotNull
  @ColumnDefault("false")
  @Column(name = "learning_disability_catered_for")
  open var learningDisabilityCateredFor: Boolean,

  @Nullable
  @Column(name = "equivalent_non_ldc_programme_guide", length = Integer.MAX_VALUE)
  open var equivalentNonLdcProgrammeGuide: String? = null,

  @Nullable
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "intervention_id", referencedColumnName = "id")
  open var intervention: InterventionCatalogue?,
)
