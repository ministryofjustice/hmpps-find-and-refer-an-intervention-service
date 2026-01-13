package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.loadmetadata

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader
import org.springframework.batch.test.MetaDataInstanceFactory
import org.springframework.transaction.PlatformTransactionManager
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.OnStartupJobLauncherFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.BatchInterventionCatalogueToCourseMap
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.LoadInterventionCatalogueToCourseMapJobConfiguration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.CourseRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueToCourseMapRepository

class InterventionCatalogueToCourserMapReaderTest {

  private val interventionCatalogueRepository = mock<InterventionCatalogueRepository>()
  private val jobRepository = mock<JobRepository>()
  private val transactionManager = mock<PlatformTransactionManager>()
  private val interventionCatalogueToCourseMapRepository = mock<InterventionCatalogueToCourseMapRepository>()
  private val courseRepository = mock<CourseRepository>()
  private val onStartupJobLauncherFactory = mock<OnStartupJobLauncherFactory>()

  private val reader = LoadInterventionCatalogueToCourseMapJobConfiguration(
    jobRepository,
    interventionCatalogueRepository,
    interventionCatalogueToCourseMapRepository,
    courseRepository,
    onStartupJobLauncherFactory,
  ).interventionCatalogueToCourseMapReader() as FlatFileItemReader<BatchInterventionCatalogueToCourseMap>

  @Test
  fun `should read and map data from CSV file`() {
    // Open the reader
    reader.open(MetaDataInstanceFactory.createStepExecution().executionContext)

    // Read the first item
    val firstItem = reader.read()

    // Assert the first item's values
    assertThat(firstItem).isNotNull
    assertThat(firstItem!!.interventionCatalogueId).isEqualTo("8380e2d6-ba49-4309-8be7-cc83bf87f372")
    assertThat(firstItem.courseId).isEqualTo("2be8daf4-f23c-43bc-9e3c-884878eba285")
    assertThat(firstItem.status).isEqualTo("")

    // Read the next item
    val secondItem = reader.read()

    // Assert the second item's values
    assertThat(secondItem).isNotNull
    assertThat(secondItem!!.interventionCatalogueId).isEqualTo("81ea44b3-1808-4f65-94c2-a4bfa3137162")
    assertThat(secondItem.courseId).isEqualTo("b4af1bbc-1b30-434a-8edc-ae4880c05def")
    assertThat(secondItem.status).isEqualTo("")
  }
}
