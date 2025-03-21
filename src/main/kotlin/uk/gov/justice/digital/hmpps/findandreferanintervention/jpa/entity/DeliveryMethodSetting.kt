package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.util.UUID

@Entity
@Table(name = "delivery_method_setting", schema = "public")
open class DeliveryMethodSetting(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @NotNull
  @Column(name = "delivery_method_id")
  open var deliveryMethodId: UUID,

  @NotNull
  @Column(name = "setting")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType::class)
  open var setting: SettingType,
)

enum class SettingType {
  COMMUNITY,
  CUSTODY,
  REMAND,
  PRE_RELEASE,
}
