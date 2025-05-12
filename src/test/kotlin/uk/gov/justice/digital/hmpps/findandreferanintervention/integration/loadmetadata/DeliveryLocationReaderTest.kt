package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.loadmetadata

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.test.MetaDataInstanceFactory
import org.springframework.transaction.PlatformTransactionManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.BatchDeliveryLocation
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.LoadDeliveryLocationJobConfiguration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryLocationRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PduRefRepository

class DeliveryLocationReaderTest {

  private val deliveryLocationRepository = mock<DeliveryLocationRepository>()
  private val jobRepository = mock<JobRepository>()
  private val transactionManager = mock<PlatformTransactionManager>()
  private val interventionCatalogueRepository = mock<InterventionCatalogueRepository>()
  private val pduRefRepository = mock<PduRefRepository>()

  private val reader = LoadDeliveryLocationJobConfiguration(
    jobRepository,
    deliveryLocationRepository,
    interventionCatalogueRepository,
    pduRefRepository,
    transactionManager,
  ).deliveryLocationReader() as FlatFileItemReader<BatchDeliveryLocation>

  @Test
  fun `should read and map data from CSV file`() {
    // Open the reader
    reader.open(MetaDataInstanceFactory.createStepExecution().executionContext)

    // Read the first item
    val firstItem = reader.read()

    // Assert the first item's values
    assertThat(firstItem).isNotNull
    assertThat(firstItem!!.id).isEqualTo("e255558c-a63c-4a48-a4eb-14783ea1b488")
    assertThat(firstItem.interventionId).isEqualTo("554b9fd0-6e66-4f1a-ad67-4f47503d3270")
    assertThat(firstItem.providerName).isEqualTo("")
    assertThat(firstItem.pduRef).isEqualTo("bedfordshire")
    assertThat(firstItem.status).isEqualTo("")

    // Read the next item
    val secondItem = reader.read()

    // Assert the second item's values
    assertThat(secondItem).isNotNull
    assertThat(secondItem!!.id).isEqualTo("47c856d1-79b7-423c-acd7-466b9e3d93e5")
    assertThat(secondItem.interventionId).isEqualTo("554b9fd0-6e66-4f1a-ad67-4f47503d3270")
    assertThat(secondItem.providerName).isEqualTo("")
    assertThat(secondItem.pduRef).isEqualTo("cambridgeshire")
    assertThat(secondItem.status).isEqualTo("")
  }
}
