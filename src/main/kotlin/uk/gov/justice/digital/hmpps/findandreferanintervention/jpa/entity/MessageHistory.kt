package uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity

import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.Type
import uk.gov.justice.digital.hmpps.findandreferanintervention.service.event.Notification
import java.util.UUID

@Entity
@Table(name = "message_history", schema = "public")
open class MessageHistory(
  @NotNull
  @Id
  @Size(max = 1)
  @Column(name = "id", length = 1)
  open var id: UUID,

  @NotNull
  @Type(value = JsonType::class)
  @Column(name = "event", length = Integer.MAX_VALUE, columnDefinition = "jsonb")
  open var event: Notification,
)
