package uk.gov.justice.digital.hmpps.findandreferanintervention.integration.jpa.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.core.io.ResourceLoader
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.jdbc.datasource.init.ScriptUtils
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.specification.getInterventionCatalogueSpecification
import java.util.UUID
import javax.sql.DataSource
import kotlin.reflect.full.memberProperties

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InterventionCatalogueFilterRepositoryTest {
  @Autowired
  private lateinit var interventionCatalogueRepository: InterventionCatalogueRepository

  @Autowired
  private lateinit var dataSource: DataSource

  @Autowired
  private lateinit var resourceLoader: ResourceLoader
  val defaultPageRequest: PageRequest = PageRequest.of(0, 10, Sort.by("name"))

  @BeforeEach
  fun beforeEach() {
    dataSource.connection.use {
      val r = resourceLoader.getResource("classpath:testData/setup.sql")
      ScriptUtils.executeSqlScript(it, r)
    }
  }

  @AfterEach
  fun afterEach() {
    dataSource.connection.use {
      val r = resourceLoader.getResource("classpath:testData/teardown.sql")
      ScriptUtils.executeSqlScript(it, r)
    }
  }

  @Nested
  @DisplayName("Filter Interventions by Intervention Type")
  inner class FilterByInterventionType {
    @Test
    fun `findAllInterventionsBy by interventionType = 'ACP' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(interventionTypes = listOf(InterventionType.ACP))
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(3)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Becoming New Me Plus: general violence offence")
    }

    @Test
    fun `findAllInterventionsBy by interventionType = 'CRS' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(interventionTypes = listOf(InterventionType.CRS))
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(3)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Accommodation")
    }

    @Test
    fun `findAllInterventionsBy by interventionType = 'SI' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(interventionTypes = listOf(InterventionType.SI))
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(3)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Better Solutions")
    }

    @Test
    fun `findAllInterventionsBy by interventionType = 'TOOLKITS' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(interventionTypes = listOf(InterventionType.TOOLKITS))
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(3)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Maps for Change")
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

      assertThat(interventions.totalElements).isEqualTo(6)
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

      assertThat(interventions.totalElements).isEqualTo(11)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Accommodation")
    }

    @Test
    fun `findAllInterventionsBy by setting = 'CUSTODY' and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(settingType = SettingType.CUSTODY)
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(4)
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

      assertThat(interventions.totalElements).isEqualTo(11)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Accommodation")
    }

    @Test
    fun `findAllInterventionsBy by allowsFemales = TRUE and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(allowsFemales = true)
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(5)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Better Solutions")
    }

    @Test
    fun `findAllInterventionsBy by allowsMales and Females = TRUE and there are interventions return a page of interventions`() {
      val specification = getInterventionCatalogueSpecification(allowsFemales = true, allowsMales = true)
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(4)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Better Solutions")
    }
  }

  @Nested
  @DisplayName("Filter Interventions by Programme Name")
  inner class FilterByProgrammeName {
    @Test
    fun `findAllInterventionsBy by programmeName = 'Dependency and Recovery' and there are interventions return a page of interventions`() {
      val specification =
        getInterventionCatalogueSpecification(programmeName = "Dependency and Recovery")
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(1)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Dependency and Recovery")
      assertThat(interventions.content[0].id).isEqualTo(UUID.fromString("c5d53fbd-b7e3-40bd-9096-6720a01a53bf"))
    }

    @Test
    fun `findAllInterventionsBy by programmeName = 'INVALID NAME' and there are no interventions return empty page of interventions`() {
      val specification =
        getInterventionCatalogueSpecification(programmeName = "INVALID NAME")
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(0)
      assertThat(interventions.content).isEmpty()
    }
  }

  @Nested
  @DisplayName("Filter Interventions by multiple filters")
  inner class FilterByMultipleFilters {
    @Test
    fun `findAllInterventionsBy by interventionType = 'CRS' AND setting = 'COMMUNITY' AND allowsMales = TRUE and there are interventions return a page of interventions`() {
      val specification =
        getInterventionCatalogueSpecification(
          interventionTypes = listOf(InterventionType.CRS),
          settingType = SettingType.COMMUNITY,
          allowsMales = true,
        )
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(3)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Accommodation")
    }

    @Test
    fun `findAllInterventionsBy by interventionType = 'ACP' and setting = 'CUSTODY' and there are interventions return a page of interventions`() {
      val specification =
        getInterventionCatalogueSpecification(
          interventionTypes = listOf(InterventionType.ACP),
          settingType = SettingType.CUSTODY,
        )
      val interventions = interventionCatalogueRepository.findAll(specification, defaultPageRequest)

      assertThat(interventions.totalElements).isEqualTo(3)
      assertThat(hasAllCatalogueProperties(interventions))
      assertThat(interventions.content[0].name).isEqualTo("Becoming New Me Plus: general violence offence")
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
      "deliveryMethod",
      "deliveryMethodSettings",
      "eligibleOffences",
      "enablingIntervention",
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
