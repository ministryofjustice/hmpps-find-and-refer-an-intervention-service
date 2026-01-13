package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import mu.KLogging
import org.springframework.batch.infrastructure.item.ItemReader
import org.springframework.stereotype.Component

@Component
class InterventionDefinitionReader(private val resourcePathOverride: String?) : ItemReader<InterventionCatalogueDefinition> {
  companion object : KLogging()

  private val defaultresourcepath = "classpath:/db/interventions/*.json"

  private var index = 0
  private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

  override fun read(): InterventionCatalogueDefinition? {
    val files: List<String> = if (resourcePathOverride.isNullOrEmpty()) {
      InterventionLoadFileReaderHelper.getResourceUrls(defaultresourcepath)
    } else {
      InterventionLoadFileReaderHelper.getResourceUrls(resourcePathOverride)
    }

    logger.info("ready to read interventions files; {} found", files.size)

    if (files.size <= index) return null

    val currentIndex = index
    index++

    val file = files[currentIndex]
    logger.info("Reading file $index/${files.size}: $file")
    return objectMapper.readValue(
      InterventionLoadFileReaderHelper.getResource(file),
      InterventionCatalogueDefinition::class.java,
    )
  }
}
