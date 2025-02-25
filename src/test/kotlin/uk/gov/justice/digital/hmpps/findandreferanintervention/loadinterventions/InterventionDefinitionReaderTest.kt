package uk.gov.justice.digital.hmpps.findandreferanintervention.loadinterventions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions.InterventionDefinitionReader

internal class InterventionDefinitionReaderTest @Autowired constructor(
) : IntegrationTestBase() {
  @Test
  fun `copes with lack of any json intervention files`() {
    val reader = InterventionDefinitionReader("classpath:/db/empty/*.json")
    assertThat(reader.read()).isNull()
  }

  @Test
  fun `reads a file if present`() {
    val reader = InterventionDefinitionReader("classpath:/db/interventions/*.json")
    val dataRead = reader.read()
    assertThat(dataRead).isNotNull
    assertThat(dataRead?.catalogue?.name).isEqualTo("Accommodation")
  }
}
