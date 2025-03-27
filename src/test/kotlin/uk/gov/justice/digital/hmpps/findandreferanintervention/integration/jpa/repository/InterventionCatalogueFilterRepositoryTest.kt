package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.jpa.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import uk.gov.justice.digital.hmpps.findandreferanintervention.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.specification.getInterventionCatalogueSpecification
import java.util.UUID
import kotlin.reflect.full.memberProperties

class InterventionCatalogueFilterRepositoryTest : IntegrationTestBase() {
  @Autowired
  private lateinit var interventionCatalogueRepository: InterventionCatalogueRepository
  val defaultPageRequest: PageRequest = PageRequest.of(0, 10, Sort.by("name"))

  @Nested
  @DisplayName("Filter Interventions by Intervention Type")
  inner class FilterByInterventionType {
    @Test
    fun `findAllInterventionsBy by interventionType = 'ACP' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(interventionTypes = listOf(InterventionType.ACP))
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(28)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Becoming New Me Plus: general violence offence")
    }

    @Test
    fun `findAllInterventionsBy by interventionType = 'CRS' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(interventionTypes = listOf(InterventionType.CRS))
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(20)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Accommodation")
    }

    @Test
    fun `findAllInterventionsBy by interventionType = 'SI' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(interventionTypes = listOf(InterventionType.SI))
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(15)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Better Solutions")
    }

    @Test
    fun `findAllInterventionsBy by interventionType = 'TOOLKITS' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(interventionTypes = listOf(InterventionType.TOOLKITS))
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(11)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Choices and Changes")
    }

    @Test
    fun `findAllInterventionsBy by interventionType = 'TOOLKITS' OR 'ACP' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(
        interventionTypes = listOf(
          InterventionType.TOOLKITS,
          InterventionType.ACP,
        ),
      )
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(39)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Becoming New Me Plus: general violence offence")
    }
  }

  @Nested
  @DisplayName("Filter Interventions by Setting")
  inner class FilterBySetting {
    @Test
    fun `findAllInterventionsBy by setting = 'COMMUNITY' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(settingType = SettingType.COMMUNITY)
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(52)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Accommodation")
    }

    @Test
    fun `findAllInterventionsBy by setting = 'CUSTODY' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(settingType = SettingType.CUSTODY)
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(22)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Becoming New Me Plus: general violence offence")
    }
  }

  @Nested
  @DisplayName("Filter Interventions by Gender")
  inner class FilterByGender {
    @Test
    fun `findAllInterventionsBy by allowsMales = TRUE and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(allowsMales = true)
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(61)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Accommodation")
    }

    @Test
    fun `findAllInterventionsBy by allowsFemales = TRUE and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(allowsFemales = true)
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(37)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Better Solutions")
    }

    @Test
    fun `findAllInterventionsBy by allowsMales and Females = TRUE and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(allowsFemales = true, allowsMales = true)
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(24)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Better Solutions")
    }
  }

  @Nested
  @DisplayName("Filter Interventions by Programme Name")
  inner class FilterByProgrammeName {
    @Test
    fun `findAllInterventionsBy by programmeName = 'Personal Wellbeing Services' and there are interventions return a page of interventions`() {
      val specification =
        getInterventionCatalogueSpecification(programmeName = "Personal Wellbeing Services")
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(1)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Personal Wellbeing Services")
      assertThat(interventions.content[0].id).isEqualTo(UUID.fromString("7c479daa-9dd2-4307-a7e7-2e2ff4c3ced1"))
    }

    @Test
    fun `findAllInterventionsBy by programmeName = 'Personal Wellbeing' and there are interventions return a page of interventions`() {
      val specification =
        getInterventionCatalogueSpecification(programmeName = "Personal Wellbeing")
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(5)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Personal Wellbeing (PWB): Emotional Wellbeing")
      assertThat(interventions.content[0].id).isEqualTo(UUID.fromString("89849559-7d02-4d1a-9813-d6e3b52fa267"))
    }
  }

  @Nested
  @DisplayName("Filter Interventions by multiple filters")
  inner class FilterByMultipleFilters {
    @Test
    fun `findAllInterventionsBy by interventionType = 'CRS' AND setting = 'COMMUNITY' AND allowsFemales = TRUE and there are interventions return a page of interventions`() {
      val specification =
        getInterventionCatalogueSpecification(
          interventionTypes = listOf(InterventionType.CRS),
          settingType = SettingType.COMMUNITY,
          allowsFemales = true,
        )
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(11)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("CLI - Peer Support")
    }

    @Test
    fun `findAllInterventionsBy by  and there are interventions return a page of interventions`() {
      val specification =
        getInterventionCatalogueSpecification(
          interventionTypes = listOf(InterventionType.ACP),
          settingType = SettingType.CUSTODY,
          allowsFemales = true,
        )
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(5)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Building Choices: high intensity")
    }
  }

  private fun hasAllCatalogueProperties(interventionCatalogue: Page<InterventionCatalogue>) {
    val expectedProperties = setOf(
      "id",
      "name",
      "shortDescription",
      "longDescription",
      "topic",
      "sessionDetail",
      "commencementDate",
      "terminationDate",
      "created",
      "createdBy",
      "lastModified",
      "lastModifiedBy",
      "interventionType",
      "timeToComplete",
      "reasonForReferral",
      "criminogenicNeeds",
      "deliveryLocations",
      "deliveryMethods",
      "eligibleOffences",
      "enablingInterventions",
      "excludedOffences",
      "exclusion",
      "interventions",
      "personalEligibility",
      "possibleOutcomes",
      "riskConsideration",
      "specialEducationalNeeds",
      "courses",
    )
    interventionCatalogue.content.forEach {
      val actualProperties = it::class.memberProperties.map { member -> member.name }.toSet()
      assertThat(actualProperties).isEqualTo(expectedProperties)
    }
  }
}
