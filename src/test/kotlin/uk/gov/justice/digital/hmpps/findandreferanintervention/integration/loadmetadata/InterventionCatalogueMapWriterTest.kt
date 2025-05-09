package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.loadmetadata

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.Chunk
import org.springframework.transaction.PlatformTransactionManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.BatchInterventionCatalogueMap
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.LoadInterventionCatalogueMapJobConfiguration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueMapRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionRepository
import java.util.*

class InterventionCatalogueMapWriterTest {

  private val interventionCatalogueRepository = mock<InterventionCatalogueRepository>()
  private val jobRepository = mock<JobRepository>()
  private val transactionManager = mock<PlatformTransactionManager>()
  private val interventionCatalogueMapRepository = mock<InterventionCatalogueMapRepository>()
  private val interventionRepository = mock<InterventionRepository>()

  private val writer = LoadInterventionCatalogueMapJobConfiguration(
    jobRepository,
    interventionCatalogueRepository,
    interventionCatalogueMapRepository,
    interventionRepository,
    transactionManager,
  ).interventionCatalogueMapWriter()

  @Test
  fun `should save new intervention catalogue map`() {
    val batchInterventionCatalogueMap = BatchInterventionCatalogueMap(
      interventionId = UUID.randomUUID().toString(),
      interventionCatalogueId = UUID.randomUUID().toString(),
      status = "",
    )

    `when`(interventionCatalogueMapRepository.findByInterventionIdAndInterventionCatalogueId(any(), any())).thenReturn(null)
    `when`(interventionCatalogueRepository.findById(any())).thenReturn(Optional.of(mock()))
    `when`(interventionRepository.findById(any())).thenReturn(Optional.of(mock()))

    writer.write(Chunk(listOf(batchInterventionCatalogueMap)))

    verify(interventionCatalogueMapRepository, times(1)).save(any())
  }

  @Test
  fun `should delete intervention catalogue map when status is D`() {
    val batchInterventionCatalogueMap = BatchInterventionCatalogueMap(
      interventionId = UUID.randomUUID().toString(),
      interventionCatalogueId = UUID.randomUUID().toString(),
      status = "D",
    )

    writer.write(Chunk(listOf(batchInterventionCatalogueMap)))

    verify(interventionCatalogueMapRepository, times(1)).deleteByInterventionCatalogueIdAndInterventionId(any(), any())
  }
}
