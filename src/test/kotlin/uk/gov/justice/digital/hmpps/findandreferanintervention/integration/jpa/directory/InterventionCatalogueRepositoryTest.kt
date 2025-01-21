package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.jpa.directory

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InterventionCatalogueRepositoryTest @Autowired constructor(
  val interventionCatalogueRepository: InterventionCatalogueRepository,
) {

  @Test
  fun `whenFindAll exists return list of interventions`() {
    val pageRequest = PageRequest.of(0, 10)
    val interventions = interventionCatalogueRepository.findAll(pageRequest)

    assertThat(interventions.size).isGreaterThan(0)
  }
}
