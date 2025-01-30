package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

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
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.util.UUID

@Entity
@Table(name = "complexity_level", schema = "public")
open class ComplexityLevel {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @NotNull
  @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
  open var title: String? = null

  @NotNull
  @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
  open var description: String? = null

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "service_category_id", nullable = false)
  open var serviceCategory: ServiceCategory? = null

  @NotNull
  @Column(name = "complexity", columnDefinition = "complexities")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var complexity: Complexities? = null

  enum class Complexities {
    LOW,
    MEDIUM,
    HIGH,
  }
}
