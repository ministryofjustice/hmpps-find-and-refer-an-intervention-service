package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.util.UUID

@Entity
@Table(name = "delivery_method_setting", schema = "public")
open class DeliveryMethodSetting {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @Column(name = "setting", columnDefinition = "setting_type not null")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var setting: SettingType? = null

  enum class SettingType {
    COMMUNITY,
    CUSTODY,
  }
}
