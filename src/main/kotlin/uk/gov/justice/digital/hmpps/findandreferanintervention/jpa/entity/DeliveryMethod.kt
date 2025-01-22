package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.NotNull
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "delivery_method", schema = "public")
open class DeliveryMethod(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @Column(name = "attendance_type")
  open var attendanceType: String? = null,

  @NotNull
  @Column(name = "intervention_id")
  open var interventionId: UUID,

  @Nullable
  @Column(name = "delivery_format")
  open var deliveryFormat: String? = null,

  @Nullable
  @Column(name = "delivery_method_description", length = Integer.MAX_VALUE)
  open var deliveryMethodDescription: String? = null,

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_method_id", referencedColumnName = "id")
  open var deliveryMethodSettings: MutableSet<DeliveryMethodSetting> = mutableSetOf(),
)
