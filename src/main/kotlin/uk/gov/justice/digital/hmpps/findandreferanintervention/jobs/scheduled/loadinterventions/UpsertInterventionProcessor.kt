package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions

import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue

@Component
@JobScope
class UpsertInterventionProcessor : ItemProcessor<InterventionCatalogDefinition, InterventionCatalogue> {
  override fun process(item: InterventionCatalogDefinition): InterventionCatalogue? {
    TODO("Not yet implemented")
  }
}
