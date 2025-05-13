package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.loadmetadata

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.transaction.PlatformTransactionManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.BatchDeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.LoadDeliveryLocationJobConfiguration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryLocationRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PduRefRepository
import java.util.*

class DeliveryLocationWriterTest {

  private val deliveryLocationRepository = mock<DeliveryLocationRepository>()
  private val interventionCatalogueRepository = mock<InterventionCatalogueRepository>()
  private val pduRefRepository = mock<PduRefRepository>()
  private val jobRepository = mock<JobRepository>()
  private val transactionManager = mock<PlatformTransactionManager>()

  private val writer: ItemWriter<BatchDeliveryLocation> = LoadDeliveryLocationJobConfiguration(
    jobRepository,
    deliveryLocationRepository,
    interventionCatalogueRepository,
    pduRefRepository,
    transactionManager,
  ).deliveryLocationWriter()

  @Test
  fun `should save new delivery location`() {
    val batchDeliveryLocation = BatchDeliveryLocation(
      id = UUID.randomUUID().toString(),
      interventionId = UUID.randomUUID().toString(),
      providerName = "Seetec Business Technology Centre Limited",
      pduRef = "bedfordhsire",
      contact = "09434343",
      status = "",
    )

    `when`(deliveryLocationRepository.findByPduRefIdAndInterventionIdAndProviderName(any(), any(), any())).thenReturn(null)
    `when`(interventionCatalogueRepository.findById(any())).thenReturn(Optional.of(mock()))
    `when`(pduRefRepository.findById(any())).thenReturn(Optional.of(mock()))

    writer.write(Chunk(listOf(batchDeliveryLocation)))

    verify(deliveryLocationRepository, times(1)).save(any())
  }

  @Test
  fun `should delete delivery location when status is D`() {
    val batchDeliveryLocation = BatchDeliveryLocation(
      id = UUID.randomUUID().toString(),
      interventionId = UUID.randomUUID().toString(),
      providerName = "Seetec Business Technology Centre Limited",
      pduRef = "bedfordhsire",
      contact = "",
      status = "D",
    )

    writer.write(Chunk(listOf(batchDeliveryLocation)))

    verify(deliveryLocationRepository, times(1)).deleteByPduRefIdAndInterventionIdAndProviderName(any(), any(), any())
  }

  @Test
  fun `should not save or delete when no changes are needed`() {
    val batchDeliveryLocation = BatchDeliveryLocation(
      id = UUID.randomUUID().toString(),
      interventionId = UUID.randomUUID().toString(),
      providerName = "Seetec Business Technology Centre Limited",
      pduRef = "bedfordhsire",
      contact = "",
      status = "",
    )

    val existingDeliveryLocation = DeliveryLocation(
      id = UUID.randomUUID(),
      providerName = "Seetec Business Technology Centre Limited",
      contact = "",
      pduRef = mock(),
      intervention = mock(),
    )

    `when`(deliveryLocationRepository.findByPduRefIdAndInterventionIdAndProviderName(any(), any(), any()))
      .thenReturn(existingDeliveryLocation)

    writer.write(Chunk(listOf(batchDeliveryLocation)))

    verify(deliveryLocationRepository, times(0)).save(any())
    verify(deliveryLocationRepository, times(0)).deleteByPduRefIdAndInterventionIdAndProviderName(any(), any(), any())
  }
}
