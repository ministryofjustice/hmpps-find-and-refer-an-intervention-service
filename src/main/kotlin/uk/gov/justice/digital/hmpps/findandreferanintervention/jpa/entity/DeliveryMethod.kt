package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "delivery_method", schema = "public")
open class DeliveryMethod {
  @Id
  @Column(name = "id", nullable = false)
  open var id: UUID? = null

  @Column(name = "delivery_method_description", length = Integer.MAX_VALUE)
  open var deliveryMethodDescription: String? = null

  @OneToMany(fetch = FetchType.LAZY)
  open var deliveryMethodSettings: MutableSet<DeliveryMethodSetting> = mutableSetOf()
}
