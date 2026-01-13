package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.loadmetadata

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.infrastructure.item.Chunk
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.OnStartupJobLauncherFactory
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.BatchInterventionCatalogueToCourseMap
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadmetadata.LoadInterventionCatalogueToCourseMapJobConfiguration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.CourseRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueToCourseMapRepository
import java.util.Optional
import java.util.UUID

class InterventionCatalogueToCourseMapWriterTest {

  private val interventionCatalogueRepository = mock<InterventionCatalogueRepository>()
  private val jobRepository = mock<JobRepository>()
  private val interventionCatalogueToCourseMapRepository = mock<InterventionCatalogueToCourseMapRepository>()
  private val courseRepository = mock<CourseRepository>()
  private val onStartupJobLauncherFactory = mock<OnStartupJobLauncherFactory>()

  private val writer = LoadInterventionCatalogueToCourseMapJobConfiguration(
    jobRepository,
    interventionCatalogueRepository,
    interventionCatalogueToCourseMapRepository,
    courseRepository,
    onStartupJobLauncherFactory,
  ).interventionCatalogueToCourseMapWriter()

  @Test
  fun `should save new intervention catalogue to courser map`() {
    val batchInterventionCatalogueMap = BatchInterventionCatalogueToCourseMap(
      interventionCatalogueId = UUID.randomUUID().toString(),
      courseId = UUID.randomUUID().toString(),
      status = "",
    )

    `when`(
      interventionCatalogueToCourseMapRepository.findByInterventionCatalogueIdAndCourseId(
        any(),
        any(),
      ),
    ).thenReturn(null)
    `when`(interventionCatalogueRepository.findById(any())).thenReturn(Optional.of(mock()))
    `when`(courseRepository.findById(any())).thenReturn(Optional.of(mock()))

    writer.write(Chunk(listOf(batchInterventionCatalogueMap)))

    verify(interventionCatalogueToCourseMapRepository, times(1)).save(any())
  }

  @Test
  fun `should delete intervention catalogue to course map when status is D`() {
    val batchInterventionCatalogueMap = BatchInterventionCatalogueToCourseMap(
      interventionCatalogueId = UUID.randomUUID().toString(),
      courseId = UUID.randomUUID().toString(),
      status = "D",
    )
    writer.write(Chunk(listOf(batchInterventionCatalogueMap)))

    verify(interventionCatalogueToCourseMapRepository, times(1)).deleteByInterventionCatalogueIdAndCourseId(
      any(),
      any(),
    )
  }
}
