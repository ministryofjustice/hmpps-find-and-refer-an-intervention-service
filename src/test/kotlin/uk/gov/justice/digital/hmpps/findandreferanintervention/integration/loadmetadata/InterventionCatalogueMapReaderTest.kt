package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.test.MetaDataInstanceFactory
import org.springframework.transaction.PlatformTransactionManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueMapRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionRepository

class InterventionCatalogueMapReaderTest {

  private val interventionCatalogueRepository = mock<InterventionCatalogueRepository>()
  private val jobRepository = mock<JobRepository>()
  private val transactionManager = mock<PlatformTransactionManager>()
  private val interventionCatalogueMapRepository = mock<InterventionCatalogueMapRepository>()
  private val interventionRepository = mock<InterventionRepository>()

  private val reader = LoadInterventionCatalogueMapJobConfiguration(
    jobRepository,
    interventionCatalogueRepository,
    interventionCatalogueMapRepository,
    interventionRepository,
    transactionManager,
  ).interventionCatalogueMapReader() as FlatFileItemReader<BatchInterventionCatalogueMap>

  @Test
  fun `should read and map data from CSV file`() {
    // Open the reader
    reader.open(MetaDataInstanceFactory.createStepExecution().executionContext)

    // Read the first item
    val firstItem = reader.read()

    // Assert the first item's values
    assertThat(firstItem).isNotNull
    assertThat(firstItem!!.interventionId).isEqualTo("98a42c61-c30f-4beb-8062-04033c376e2d")
    assertThat(firstItem.interventionCatalogueId).isEqualTo("47b0048c-cab2-4509-a64a-29a4218712c9")
    assertThat(firstItem.status).isEqualTo("")

    // Read the next item
    val secondItem = reader.read()

    // Assert the second item's values
    assertThat(secondItem).isNotNull
    assertThat(secondItem!!.interventionId).isEqualTo("2cee9965-0247-4459-9e68-40bc73984eaa")
    assertThat(secondItem.interventionCatalogueId).isEqualTo("47b0048c-cab2-4509-a64a-29a4218712c9")
    assertThat(secondItem.status).isEqualTo("")
  }
}
