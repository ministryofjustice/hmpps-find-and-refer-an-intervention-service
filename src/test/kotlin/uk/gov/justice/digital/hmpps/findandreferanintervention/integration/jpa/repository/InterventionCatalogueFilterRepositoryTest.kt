package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.jpa.repository

import au.com.dius.pact.core.support.contains
import au.com.dius.pact.core.support.hasProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepositoryImpl

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InterventionCatalogueFilterRepositoryTest
@Autowired
constructor(
  private val interventionCatalogueRepositoryImpl: InterventionCatalogueRepositoryImpl,
) {

  @Nested
  @DisplayName("Filter Interventions by Intervention Type")
  inner class FilterByInterventionType {
    @Test
    fun `findAllInterventionCatalogueByCriteria by interventionType = 'ACP' and there are interventions return a page of interventions`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = null,
          allowsMales = null,
          interventionTypes = listOf(InterventionType.ACP),
          settingType = null,
          programmeName = null,
        )

      assertThat(interventions.totalElements).isEqualTo(5)
      assertThat(hasAllCatalogueProperties(interventions)).isTrue()
      assertThat(interventions.content[0].name).isEqualTo("Building Better Relationships")
    }

    @Test
    fun `findAllInterventionCatalogueByCriteria by interventionType = 'SI' and there are no interventions return an empty page of interventions`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = null,
          allowsMales = null,
          interventionTypes = listOf(InterventionType.SI),
          settingType = null,
          programmeName = null,
        )

      assertThat(interventions.totalElements).isEqualTo(0)
      assertThat(interventions.content).isEmpty()
    }

    @Test
    fun `findAllInterventionCatalogueByCriteria by interventionType = 'ACP' AND interventionType = 'CRS' and there are interventions return a page of interventions`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = null,
          allowsMales = null,
          interventionTypes = listOf(InterventionType.ACP, InterventionType.CRS),
          settingType = null,
          programmeName = null,
        )

      assertThat(interventions.totalElements).isEqualTo(9)
      assertThat(hasAllCatalogueProperties(interventions)).isTrue()
      assertThat(interventions.content[0].name).isEqualTo("Building Better Relationships")
      assertThat(assertThat(interventions.content[0].interventionType).isEqualTo(InterventionType.ACP))
      assertThat(interventions.content[5].name).isEqualTo("Accommodation")
      assertThat(assertThat(interventions.content[5].interventionType).isEqualTo(InterventionType.CRS))
    }
  }

  @Nested
  @DisplayName("Filter Interventions by setting")
  inner class FilterBySetting {
    @Test
    fun `findAllInterventionCatalogueByCriteria by settingType = 'COMMUNITY' and there are interventions return a page of interventions`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = null,
          allowsMales = null,
          interventionTypes = listOf(InterventionType.ACP, InterventionType.CRS),
          settingType = SettingType.COMMUNITY,
          programmeName = null,
        )

      assertThat(interventions.totalElements).isEqualTo(5)
      assertThat(hasAllCatalogueProperties(interventions)).isTrue()
      assertThat(interventions.content[0].name).isEqualTo("Building Better Relationships")
      assertThat(assertThat(interventions.content[0].interventionType).isEqualTo(InterventionType.ACP))
    }
  }

  @Nested
  @DisplayName("Filter Interventions by gender")
  inner class FilterByGender {
    @Test
    fun `findInterventionByGender = 'allow males' and there are interventions return a page of interventions`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = null,
          allowsMales = true,
          interventionTypes = null,
          settingType = null,
          programmeName = null,
        )

      assertThat(interventions.totalElements).isEqualTo(9)
      assertThat(hasAllCatalogueProperties(interventions)).isTrue()
      assertThat(interventions.content.all { it.personalEligibility!!.males }).isTrue()
    }

    @Test
    fun `findInterventionByGender = 'allow females' and there are interventions return a page of interventions`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = true,
          allowsMales = null,
          interventionTypes = null,
          settingType = null,
          programmeName = null,
        )

      assertThat(interventions.totalElements).isEqualTo(2)
      assertThat(hasAllCatalogueProperties(interventions)).isTrue()
      assertThat(interventions.content.all { it.personalEligibility!!.females }).isTrue()
    }

    @Test
    fun `findInterventionByGender = 'allow females' & 'allow males' and there are interventions return a page of interventions allowing both genders`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = true,
          allowsMales = true,
          interventionTypes = null,
          settingType = null,
          programmeName = null,
        )

      assertThat(interventions.totalElements).isEqualTo(2)
      assertThat(hasAllCatalogueProperties(interventions)).isTrue()
      assertThat(interventions.content.all { it.personalEligibility!!.females }).isTrue()
      assertThat(interventions.content.all { it.personalEligibility!!.males }).isTrue()
    }
  }

  @Nested
  @DisplayName("Filter Interventions by programme name")
  inner class FilterByProgrammeName {
    @Test
    fun `FilterByProgrammeName = 'programme name' and there are interventions return a page of interventions`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = null,
          allowsMales = true,
          interventionTypes = null,
          settingType = null,
          programmeName = "bEtTer",
        )

      assertThat(interventions.totalElements).isEqualTo(1)
      assertThat(hasAllCatalogueProperties(interventions)).isTrue()
      assertThat(interventions.content.all { it.name.contains("Better") }).isTrue()
    }
  }

  @Nested
  @DisplayName("Multiple filters")
  inner class FilterByMultiple {
    @Test
    fun `findInterventionByTypeSettingGenderAndProgrammeName and there are interventions return a page of interventions`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = null,
          allowsMales = true,
          interventionTypes = listOf(InterventionType.ACP),
          settingType = SettingType.CUSTODY,
          programmeName = "Healthy",
        )

      assertThat(interventions.totalElements).isEqualTo(1)
      assertThat(hasAllCatalogueProperties(interventions)).isTrue()
      assertThat(interventions.content.all { it.personalEligibility!!.males }).isTrue()
      assertThat(interventions.content.all { it.interventionType == InterventionType.ACP })
      assertThat(interventions.content.all { it.name.contains("Healthy") })
    }

    @Test
    fun `findInterventionByTypeSettingGenderButNotProgrammeName and there are interventions return a page of interventions`() {
      val pageRequest = PageRequest.of(0, 10)
      val interventions =
        interventionRepositoryImpl.findAllInterventionCatalogueByCriteria(
          pageable = pageRequest,
          allowsFemales = null,
          allowsMales = true,
          interventionTypes = listOf(InterventionType.ACP),
          settingType = SettingType.CUSTODY,
          programmeName = null,
        )

      assertThat(interventions.totalElements).isEqualTo(2)
      assertThat(hasAllCatalogueProperties(interventions)).isTrue()
      assertThat(interventions.content.all { it.personalEligibility!!.males }).isTrue()
      assertThat(interventions.content.all { it.interventionType == InterventionType.ACP })
    }
  }

  @Test
  fun `findAllInterventionCatalogueByCriteria with no criteria and there are interventions return a page of all interventions`() {
    val pageRequest = PageRequest.of(0, 10)
    val interventions =
      interventionCatalogueRepositoryImpl.findAllInterventionCatalogueByCriteria(
        pageable = pageRequest,
        allowsFemales = null,
        allowsMales = null,
        interventionTypes = null,
        settingType = null,
        programmeName = null,
      )

    assertThat(interventions.totalElements).isEqualTo(9)
    assertThat(hasAllCatalogueProperties(interventions)).isTrue()
    assertThat(interventions.content[0].name).isEqualTo("Building Better Relationships")
  }

  private fun hasAllCatalogueProperties(interventionCatalogue: Page<InterventionCatalogue>): Boolean = interventionCatalogue.content.all {
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
    it.hasProperty("reasonsForReferral")
    it.hasProperty("timeToComplete")
  }
}
