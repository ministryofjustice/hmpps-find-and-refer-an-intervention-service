package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType
import java.util.UUID

@Entity
@Table(name = "complexity_level", schema = "public")
open class ComplexityLevel(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @Column(name = "title", length = Integer.MAX_VALUE)
  open var title: String,

  @NotNull
  @Column(name = "description", length = Integer.MAX_VALUE)
  open var description: String,

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "service_category_id")
  open var serviceCategory: ServiceCategory,

  @Nullable
  @Column(name = "complexity", columnDefinition = "complexities")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var complexity: Complexities? = null,
)

enum class Complexities {
  LOW,
  MEDIUM,
  HIGH,
}
