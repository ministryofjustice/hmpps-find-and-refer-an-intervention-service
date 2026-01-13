package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import jakarta.annotation.Nullable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.event.SqsMessage
import java.util.UUID

@Entity
@Table(name = "message", schema = "public")
open class Message(
  @NotNull
  @Id
  @Column(name = "id")
  open var id: UUID,

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "referral_id")
  open var referral: Referral? = null,

  @NotNull
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "event", columnDefinition = "jsonb")
  open var event: SqsMessage,
)
