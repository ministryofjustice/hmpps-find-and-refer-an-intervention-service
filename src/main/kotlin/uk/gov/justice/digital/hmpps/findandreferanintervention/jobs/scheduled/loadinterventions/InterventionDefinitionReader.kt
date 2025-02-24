package uk.gov.justice.digital.hmpps.hmppsinterventionsservice.jobs.scheduled.loadinterventions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import mu.KLogging
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component

@Component
@JobScope
class InterventionDefinitionReader : ItemReader<InterventionCatalogDefinition> {
  companion object : KLogging()

  val files = InterventionLoadFileReaderHelper.getResourceUrls("classpath:/interventions/*.json")
  private var index = 0
  private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())

  override fun read(): InterventionCatalogDefinition? {
    logger.info("ready to read interventions files; {} found", files.size)

    if (files.size <= index) return null

    val currentIndex = index
    index++

    val file = files[currentIndex]
    logger.info("Reading file $index/${this.files.size}: $file")
    return objectMapper.readValue(InterventionLoadFileReaderHelper.getResource(file), InterventionCatalogDefinition::class.java)
  }
}
