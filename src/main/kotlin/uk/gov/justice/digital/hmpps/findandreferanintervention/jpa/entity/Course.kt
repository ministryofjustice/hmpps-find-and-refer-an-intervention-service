package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.ColumnDefault
import java.util.UUID

@Entity
@Table(name = "course", schema = "public")
open class Course(
  @NotNull
  @Id
  @Column(name = "course_id")
  open var id: UUID,

  @NotNull
  @Column(name = "name", length = Integer.MAX_VALUE)
  open var name: String,

  @Nullable
  @Column(name = "description", length = Integer.MAX_VALUE)
  open var description: String? = null,

  @Nullable
  @Column(name = "alternate_name", length = Integer.MAX_VALUE)
  open var alternateName: String? = null,

  @NotNull
  @ColumnDefault("''")
  @Column(name = "identifier", length = Integer.MAX_VALUE)
  open var identifier: String,

  @NotNull
  @ColumnDefault("false")
  @Column(name = "withdrawn")
  open var withdrawn: Boolean = false,

  @Nullable
  @Column(name = "audience", length = Integer.MAX_VALUE)
  open var audience: String? = null,

  @Nullable
  @Column(name = "audience_colour", length = Integer.MAX_VALUE)
  open var audienceColour: String? = null,

  @Nullable
  @Column(name = "list_display_name", length = Integer.MAX_VALUE)
  open var listDisplayName: String? = null,

  @NotNull
  @ColumnDefault("0")
  @Column(name = "version")
  open var version: Long = 0,

  @NotNull
  @ColumnDefault("false")
  @Column(name = "display_on_programme_directory")
  open var displayOnProgrammeDirectory: Boolean = false,

  @Nullable
  @Column(name = "intensity", length = Integer.MAX_VALUE)
  open var intensity: String? = null,

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
  open var prerequisite: MutableSet<Prerequisite> = mutableSetOf(),

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
  open var offering: MutableSet<Offering> = mutableSetOf(),

  @ManyToMany
  @JoinTable(
    name = "intervention_catalogue_to_course_map",
    joinColumns = [JoinColumn(name = "course_id")],
    inverseJoinColumns = [JoinColumn(name = "intervention_catalogue_id")],
  )
  open var interventionCatalogues: MutableSet<InterventionCatalogue> = mutableSetOf(),
)
