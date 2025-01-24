package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.jpa.directory

import au.com.dius.pact.core.support.hasProperty
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
  fun `whenFindAll with pageRequest and there are interventions return a page of interventions`() {
    val pageRequest = PageRequest.of(0, 10)
    val interventions = interventionCatalogueRepository.findAll(pageRequest)

    assertThat(interventions.totalElements).isGreaterThan(0)
    assertThat(
      interventions.content.all {
        it.hasProperty("name")
        it.hasProperty("shortDescription")
        it.hasProperty("intType")
        it.hasProperty("criminogenicNeeds")
        it.hasProperty("deliveryLocations")
        it.hasProperty("deliveryMethods")
        it.hasProperty("eligibleOffences")
        it.hasProperty("enablingInterventions")
        it.hasProperty("excludedOffences")
        it.hasProperty("exclusion")
        it.hasProperty("personalEligibility")
        it.hasProperty("possibleOutcomes")
        it.hasProperty("riskConsideration")
        it.hasProperty("specialEducationalNeeds")
      },
    )
  }

  @Test
  fun `whenFindAll and there are interventions return a list of interventions`() {
    val interventions = interventionCatalogueRepository.findAll()

    assertThat(interventions.size).isGreaterThan(0)
    assertThat(
      interventions.all {
        it.hasProperty("name")
        it.hasProperty("shortDescription")
        it.hasProperty("intType")
        it.hasProperty("criminogenicNeeds")
        it.hasProperty("deliveryLocations")
        it.hasProperty("deliveryMethods")
        it.hasProperty("eligibleOffences")
        it.hasProperty("enablingInterventions")
        it.hasProperty("excludedOffences")
        it.hasProperty("exclusion")
        it.hasProperty("personalEligibility")
        it.hasProperty("possibleOutcomes")
        it.hasProperty("riskConsideration")
        it.hasProperty("specialEducationalNeeds")
      },
    )
  }
}
