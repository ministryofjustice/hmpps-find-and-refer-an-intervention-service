package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.jpa.repository

import au.com.dius.pact.core.support.hasProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepositoryImpl

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InterventionCatalogueFilterRepositoryTest
@Autowired
constructor(
  private val interventionCatalogueRepositoryImpl: InterventionCatalogueRepositoryImpl,
) {

  @Test
  fun `findAllInterventionCatalogueByCriteria by interventionType = 'ACP' and there are interventions return a page of interventions`() {
    val pageRequest = PageRequest.of(0, 10)
    val interventions =
      interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
        interventionType = InterventionType.ACP,
        pageable = pageRequest,
      )

    assertThat(interventions.totalElements).isEqualTo(5)
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
    assertThat(interventions.content[0].name).isEqualTo("Building Better Relationships")
  }

  @Test
  fun `findAllInterventionCatalogueByCriteria by interventionType = 'SI' and there are no interventions return an empty page of interventions`() {
    val pageRequest = PageRequest.of(0, 10)
    val interventions =
      interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
        interventionType = InterventionType.SI,
        pageable = pageRequest,
      )

    assertThat(interventions.totalElements).isEqualTo(0)
    assertThat(interventions.content).isEmpty()
  }

  @Test
  fun `findAllInterventionCatalogueByCriteria with no criteria and there are interventions return a page of all interventions`() {
    val pageRequest = PageRequest.of(0, 10)
    val interventions =
      interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
        interventionType = null,
        pageable = pageRequest,
      )

    assertThat(interventions.totalElements).isEqualTo(9)
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
    assertThat(interventions.content[0].name).isEqualTo("Building Better Relationships")
  }
}
