package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions

import mu.KLogging
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.AuthUser
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeed
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeedRef
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethod
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryMethodSetting
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EligibleOffence
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.EnablingIntervention
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.ExcludedOffence
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.Exclusion
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionCatalogue
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.InterventionType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.OffenceTypeRef
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PersonalEligibility
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.PossibleOutcome
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RiskConsideration
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.RoshLevel
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SettingType
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.SpecialEducationalNeed
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.CriminogenicNeedRefRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.CriminogenicNeedRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryLocationRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryMethodRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.DeliveryMethodSettingRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.EligibleOffenceRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.EnablingInterventionRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.ExcludedOffenceRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.ExclusionRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.InterventionCatalogueRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.OffenceTypeRefRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PduRefRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PersonalEligibilityRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.PossibleOutcomeRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.RiskConsiderationRepository
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.repository.SpecialEducationalNeedRepository
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Component
@JobScope
class UpsertInterventionProcessor(
  private val interventionCatalogueRepository: InterventionCatalogueRepository,
  private val criminogenicNeedRepository: CriminogenicNeedRepository,
  private val criminogenicNeedRefRepository: CriminogenicNeedRefRepository,
  private val deliveryLocationRepository: DeliveryLocationRepository,
  private val pduRefRepository: PduRefRepository,
  private val deliveryMethodRepository: DeliveryMethodRepository,
  private val deliveryMethodSettingRepository: DeliveryMethodSettingRepository,
  private val eligibleOffenceRepository: EligibleOffenceRepository,
  private val offenceTypeRefRepository: OffenceTypeRefRepository,
  private val enablingInterventionRepository: EnablingInterventionRepository,
  private val excludedOffenceRepository: ExcludedOffenceRepository,
  private val exclusionRepository: ExclusionRepository,
  private val personalEligibilityRepository: PersonalEligibilityRepository,
  private val possibleOutcomeRepository: PossibleOutcomeRepository,
  private val riskConsiderationRepository: RiskConsiderationRepository,
  private val specialEducationalNeedRepository: SpecialEducationalNeedRepository,
) : ItemProcessor<InterventionCatalogueDefinition, InterventionCatalogue> {
  companion object : KLogging()

  override fun process(item: InterventionCatalogueDefinition): InterventionCatalogue? {
    logger.info("Processing Intervention Catalogue item - ${item.catalogue.name}")

    when {
      item.uuid.toString().isBlank() -> {
        logger.info("Unable to create an Intervention Catalogue Entry as UUID was not provided")
        return null
      }

      item.catalogue.name.isBlank() -> {
        logger.info("Unable to create an Intervention Catalogue Entry as Name was not provided")
        return null
      }

      !checkInterventionType(item) -> {
        logger.info("Unable to create an Intervention Catalogue Entry as Type is invalid or was not provided")
        return null
      }

      item.deliveryMethodSetting.isEmpty() -> {
        logger.info("Unable to create an Intervention Catalogue Entry as SettingTypes was not provided")
        return null
      }

      else -> logger.info("Creating Intervention Catalogue Record - ${item.catalogue.name}")
    }

    return createInterventionCatalogueEntry(item)
  }

  fun checkInterventionType(item: InterventionCatalogueDefinition): Boolean {
    val interventionTypes = InterventionType.entries.map(InterventionType::name)
    return interventionTypes.contains(item.catalogue.interventionType.uppercase())
  }

  fun createInterventionCatalogueEntry(item: InterventionCatalogueDefinition): InterventionCatalogue {
    val catalogueEntry = interventionCatalogueRepository.findInterventionCatalogueById(item.uuid)

    when {
      catalogueEntry != null -> {
        logger.info(
          "Retrieved Intervention Catalogue Entry record from Database - ${item.catalogue.name}, id - ${item.uuid}",
        )

        catalogueEntry.name = item.catalogue.name
        catalogueEntry.shortDescription = item.catalogue.shortDescription
        catalogueEntry.longDescription = item.catalogue.longDescription
        catalogueEntry.topic = item.catalogue.topic
        catalogueEntry.sessionDetail = item.catalogue.sessionDetail
        catalogueEntry.commencementDate = LocalDate.now()
        catalogueEntry.created = OffsetDateTime.now()
        catalogueEntry.createdBy = AuthUser.interventionsServiceUser
        catalogueEntry.interventionType = InterventionType.valueOf(item.catalogue.interventionType.uppercase())
        catalogueEntry.timeToComplete = item.catalogue.timeToComplete
        catalogueEntry.reasonForReferral = item.catalogue.reasonForReferral

        return createInterventionCatalogue(item, catalogueEntry)
      }

      else -> {
        logger.info(
          "Inserting Intervention Catalogue Entry - ${item.catalogue.name}, id - ${item.uuid}",
        )
        val newCatalogueEntry = insertInterventionCatalogueEntry(item.catalogue, item.uuid)
        return createInterventionCatalogue(item, newCatalogueEntry)
      }
    }
  }

  fun insertInterventionCatalogueEntry(
    catalogue: InterventionCatalogueEntryDefinition,
    catalogueId: UUID,
  ): InterventionCatalogue {
    val newCatalogueRecord = InterventionCatalogue(
      id = catalogueId,
      name = catalogue.name,
      shortDescription = catalogue.shortDescription,
      longDescription = catalogue.longDescription,
      topic = catalogue.topic,
      sessionDetail = catalogue.sessionDetail,
      commencementDate = LocalDate.now(),
      created = OffsetDateTime.now(),
      createdBy = AuthUser.interventionsServiceUser,
      interventionType = InterventionType.valueOf(catalogue.interventionType.uppercase()),
      timeToComplete = catalogue.timeToComplete,
      reasonForReferral = catalogue.reasonForReferral,
      criminogenicNeeds = mutableSetOf(),
      deliveryLocations = mutableSetOf(),
      deliveryMethods = mutableSetOf(),
      eligibleOffences = mutableSetOf(),
      enablingInterventions = mutableSetOf(),
      excludedOffences = mutableSetOf(),
      exclusion = null,
      personalEligibility = null,
      possibleOutcomes = mutableSetOf(),
      riskConsideration = null,
      specialEducationalNeeds = null,
    )

    logger.info("Inserted new Intervention Catalogue Entry record into Database - ${catalogue.name}, id - $catalogueId")
    return interventionCatalogueRepository.save(newCatalogueRecord)
  }

  fun createInterventionCatalogue(
    item: InterventionCatalogueDefinition,
    catalogue: InterventionCatalogue,
  ): InterventionCatalogue {
    if (item.criminogenicNeed?.isNotEmpty() == true) {
      catalogue.criminogenicNeeds = upsertCriminogenicNeeds(item.criminogenicNeed, catalogue)
    }
    if (item.deliveryMethod?.isNotEmpty() == true) {
      catalogue.deliveryMethods = upsertDeliveryMethods(item.deliveryMethod, catalogue)
    }
    if (item.deliveryMethodSetting.isNotEmpty()) {
      upsertDeliveryMethodSettings(item.deliveryMethodSetting, catalogue)
    }
    if (item.eligibleOffence?.isNotEmpty() == true) {
      catalogue.eligibleOffences = upsertEligibleOffences(item.eligibleOffence, catalogue)
    }
    if (item.enablingIntervention != null) {
      catalogue.enablingInterventions = upsertEnablingInterventions(item.enablingIntervention, catalogue)
    }
    if (item.excludedOffence?.isNotEmpty() == true) {
      catalogue.excludedOffences = upsertExcludedOffences(item.excludedOffence, catalogue)
    }
    if (item.exclusion != null) {
      catalogue.exclusion = upsertExclusion(item.exclusion, catalogue)
    }
    if (item.personalEligibility.toString().isNotEmpty()) {
      catalogue.personalEligibility = upsertPersonalEligibility(item.personalEligibility, catalogue)
    }
    if (item.possibleOutcome?.isNotEmpty() == true) {
      catalogue.possibleOutcomes = upsertPossibleOutcomes(item.possibleOutcome, catalogue)
    }
    if (item.riskConsideration != null) {
      catalogue.riskConsideration = upsertRiskConsideration(item.riskConsideration, catalogue)
    }
    if (item.specialEducationalNeed != null) {
      catalogue.specialEducationalNeeds = upsertSpecialEducationalNeed(item.specialEducationalNeed, catalogue)
    }

    logger.info("Finished inserting Intervention Catalogue Record - ${catalogue.name}, id - ${catalogue.id}")
    return interventionCatalogueRepository.save(catalogue)
  }

  fun upsertCriminogenicNeeds(
    criminogenicNeeds: Array<String>,
    catalogue: InterventionCatalogue,
  ): MutableSet<CriminogenicNeed> {
    val criminogenicNeedRecords: MutableList<CriminogenicNeed> =
      criminogenicNeedRepository.findByIntervention(catalogue)?.toMutableList() ?: mutableListOf()

    fun createCriminogenicNeeds() {
      criminogenicNeeds.forEach { needReference ->
        val criminogenicReference: CriminogenicNeedRef? = criminogenicNeedRefRepository.findByName(needReference)

        criminogenicNeedRecords.add(
          CriminogenicNeed(
            id = UUID.randomUUID(),
            need = criminogenicReference ?: criminogenicNeedRefRepository.save(
              CriminogenicNeedRef(
                UUID.randomUUID(),
                needReference,
              ),
            ),
            intervention = catalogue,
          ),
        )
      }
    }

    when {
      criminogenicNeedRecords.isNotEmpty() -> {
        logger.info(
          "Retrieved and removed ${criminogenicNeedRecords.size} Criminogenic Needs from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        criminogenicNeedRepository.deleteAllByIntervention(catalogue)
        criminogenicNeedRecords.removeAll(criminogenicNeedRecords)
        createCriminogenicNeeds()
      }

      else -> {
        createCriminogenicNeeds()
      }
    }

    logger.info(
      "Inserted ${criminogenicNeedRecords.size} Criminogenic Need records into Database for Intervention Catalogue Entry - " +
        "${catalogue.name}, id - ${catalogue.id}",
    )

    return criminogenicNeedRepository.saveAll(criminogenicNeedRecords).toMutableSet()
  }

  fun upsertDeliveryMethods(
    deliveryMethods: Array<DeliveryMethodDefinition>,
    catalogue: InterventionCatalogue,
  ): MutableSet<DeliveryMethod> {
    val deliveryMethodRecords: MutableList<DeliveryMethod> =
      deliveryMethodRepository.findByIntervention(catalogue)?.toMutableList() ?: mutableListOf()

    fun createDeliveryMethods() {
      deliveryMethods.forEach { deliveryMethod ->
        deliveryMethodRecords.add(
          DeliveryMethod(
            id = UUID.randomUUID(),
            attendanceType = deliveryMethod.attendanceType,
            deliveryFormat = deliveryMethod.deliveryFormat,
            deliveryMethodDescription = deliveryMethod.deliveryMethodDescription,
            intervention = catalogue,
          ),
        )
      }
    }

    when {
      deliveryMethodRecords.isNotEmpty() -> {
        logger.info(
          "Retrieved and removed ${deliveryMethodRecords.size} Delivery Method records from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        deliveryMethodRepository.deleteAllByIntervention(catalogue)
        deliveryMethodRecords.removeAll(deliveryMethodRecords)
        createDeliveryMethods()
      }
      else -> {
        createDeliveryMethods()
      }
    }

    logger.info(
      "Inserted ${deliveryMethodRecords.size} Delivery Method records into Database for Intervention Catalogue Entry - " +
        "${catalogue.name}, id - ${catalogue.id}",
    )

    return deliveryMethodRepository.saveAll(deliveryMethodRecords).toMutableSet()
  }

  fun getSettingTypeForSettingName(settingName: String?): SettingType? {
    val settingNameUppercased: String? = settingName?.uppercase()
    val settingTypes: List<String> = SettingType.entries.map(SettingType::name)
    var preRelease: String? = null

    if (settingNameUppercased.equals("PRE-RELEASE")) preRelease = "PRE_RELEASE"

    return when {
      settingTypes.contains(settingNameUppercased) -> {
        SettingType.entries[settingTypes.indexOf(settingName)]
      }

      settingTypes.contains(preRelease) -> {
        SettingType.entries[settingTypes.indexOf(preRelease)]
      }

      else -> {
        null
      }
    }
  }

  fun upsertDeliveryMethodSettings(
    deliveryMethodSettings: Array<String>,
    catalogue: InterventionCatalogue,
  ): MutableSet<DeliveryMethodSetting> {
    val deliveryMethodSettingRecords: MutableList<DeliveryMethodSetting> =
      deliveryMethodSettingRepository.findByIntervention(catalogue)?.toMutableList() ?: mutableListOf()

    fun createDeliveryMethodSettings() {
      deliveryMethodSettings.forEach { deliveryMethodSetting ->
        val settingType = getSettingTypeForSettingName(deliveryMethodSetting)

        when {
          settingType != null -> {
            deliveryMethodSettingRecords.add(
              DeliveryMethodSetting(
                id = UUID.randomUUID(),
                intervention = catalogue,
                setting = settingType,
              ),
            )
          }
          else -> {
            logger.info(
              "Unable to create Delivery Method Setting - '$deliveryMethodSetting', " +
                "for Intervention Catalogue Entry - ${catalogue.name}, id - ${catalogue.id}, " +
                "as SettingType is null or invalid",
            )
          }
        }
      }
    }

    when {
      deliveryMethodSettingRecords.isNotEmpty() -> {
        logger.info(
          "Retrieved and removed ${deliveryMethodSettingRecords.size} Delivery Method Setting records from Database for Intervention Catalogue Entry " +
            "- ${catalogue.name}, id - ${catalogue.id}",
        )

        deliveryMethodSettingRepository.deleteAllByIntervention(catalogue)
        deliveryMethodSettingRecords.removeAll(deliveryMethodSettingRecords)
        createDeliveryMethodSettings()
      }
      else -> {
        createDeliveryMethodSettings()
      }
    }

    return if (deliveryMethodSettingRecords.isNotEmpty()) {
      logger.info(
        "Inserted ${deliveryMethodSettingRecords.size} Delivery Method Setting records into Database for Intervention Catalogue Entry - " +
          "${catalogue.name}, id - ${catalogue.id}",
      )
      deliveryMethodSettingRepository.saveAll(deliveryMethodSettingRecords).toMutableSet()
    } else {
      mutableSetOf()
    }
  }

  fun upsertEligibleOffences(
    eligibleOffences: Array<EligibleOffenceDefinition>,
    catalogue: InterventionCatalogue,
  ): MutableSet<EligibleOffence> {
    val eligibleOffenceRecords: MutableList<EligibleOffence> =
      eligibleOffenceRepository.findByIntervention(catalogue)?.toMutableList() ?: mutableListOf()

    fun createEligibleOffences() {
      eligibleOffences.forEach { eligibleOffence ->
        val offenceType = offenceTypeRefRepository.findByName(eligibleOffence.offenceTypeId)

        eligibleOffenceRecords.add(
          EligibleOffence(
            id = UUID.randomUUID(),
            offenceType = offenceType ?: offenceTypeRefRepository.save(
              OffenceTypeRef(
                UUID.randomUUID(),
                eligibleOffence.offenceTypeId,
              ),
            ),
            victimType = eligibleOffence.victimType,
            motivation = eligibleOffence.motivation,
            intervention = catalogue,
          ),
        )
      }
    }

    when {
      eligibleOffenceRecords.isNotEmpty() -> {
        logger.info(
          "Retrieved and removed ${eligibleOffenceRecords.size} Eligible Offence records from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        eligibleOffenceRepository.deleteAllByIntervention(catalogue)
        eligibleOffenceRecords.removeAll(eligibleOffenceRecords)
        createEligibleOffences()
      }
      else -> {
        createEligibleOffences()
      }
    }

    logger.info(
      "Inserted ${eligibleOffenceRecords.size} Eligible Offence records for Intervention Catalogue Entry - " +
        "${catalogue.name}, id - ${catalogue.id}",
    )

    return eligibleOffenceRepository.saveAll(eligibleOffenceRecords).toMutableSet()
  }

  fun upsertEnablingInterventions(
    details: String?,
    catalogue: InterventionCatalogue,
  ): MutableSet<EnablingIntervention> {
    val enablingInterventionRecords: MutableList<EnablingIntervention> =
      enablingInterventionRepository.findByIntervention(catalogue)?.toMutableList() ?: mutableListOf()

    when {
      enablingInterventionRecords.isNotEmpty() -> {
        logger.info(
          "Retrieved ${enablingInterventionRecords.size} Enabling Intervention records from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        return enablingInterventionRecords.toMutableSet()
      }

      else -> {
        details?.let {
          enablingInterventionRecords.add(
            EnablingIntervention(
              id = UUID.randomUUID(),
              enablingInterventionDetail = details,
              intervention = catalogue,
            ),
          )
        }

        return if (enablingInterventionRecords.isNotEmpty()) {
          logger.info(
            "Inserted ${enablingInterventionRecords.size} Enabling Intervention records for Intervention Catalogue Entry - " +
              "${catalogue.name}, id - ${catalogue.id}",
          )
          enablingInterventionRepository.saveAll(enablingInterventionRecords).toMutableSet()
        } else {
          mutableSetOf()
        }
      }
    }
  }

  fun upsertExcludedOffences(
    excludedOffences: Array<ExcludedOffencesDefinition>,
    catalogue: InterventionCatalogue,
  ): MutableSet<ExcludedOffence> {
    val excludedOffenceRecords: MutableList<ExcludedOffence> =
      excludedOffenceRepository.findByIntervention(catalogue)?.toMutableList() ?: mutableListOf()

    fun createExcludedOffences() {
      excludedOffences.forEach { excludedOffence ->
        val offenceType: OffenceTypeRef? = offenceTypeRefRepository.findByName(excludedOffence.offenceTypeId)

        excludedOffenceRecords.add(
          ExcludedOffence(
            id = UUID.randomUUID(),
            offenceType = offenceType ?: offenceTypeRefRepository.save(
              OffenceTypeRef(
                UUID.randomUUID(),
                excludedOffence.offenceTypeId,
              ),
            ),
            victimType = excludedOffence.victimType,
            motivation = excludedOffence.motivation,
            intervention = catalogue,
          ),
        )
      }
    }

    when {
      excludedOffenceRecords.isNotEmpty() -> {
        logger.info(
          "Retrieved and removed ${excludedOffenceRecords.size} Excluded Offence records from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        excludedOffenceRepository.deleteAllByIntervention(catalogue)
        excludedOffenceRecords.removeAll(excludedOffenceRecords)
        createExcludedOffences()
      }
      else -> {
        createExcludedOffences()
      }
    }

    logger.info(
      "Inserted ${excludedOffenceRecords.size} Excluded Offence records into Database for Intervention Catalogue Entry - " +
        "${catalogue.name}, id - ${catalogue.id}",
    )

    return excludedOffenceRepository.saveAll(excludedOffenceRecords).toMutableSet()
  }

  fun upsertExclusion(
    exclusion: ExclusionDefinition?,
    catalogue: InterventionCatalogue,
  ): Exclusion? {
    val exclusionRecord: Exclusion? = exclusionRepository.findByIntervention(catalogue)
    return if (exclusionRecord !== null) {
      logger.info(
        "Retrieved Exclusion record from Database for Intervention Catalogue Entry - " +
          "${catalogue.name}, id - ${catalogue.id}",
      )

      exclusionRecord.minRemainingSentenceDurationGuide = exclusion?.minRemainingSentenceGuide
      exclusionRecord.remainingLicenseCommunityOrderGuide = exclusion?.remainingLicenseCommunityOrderGuide
      exclusionRecord.alcoholDrugProblemGuide = exclusion?.alcoholDrugProblemGuide
      exclusionRecord.mentalHealthProblemGuide = exclusion?.mentalHealthProblemGuide
      exclusionRecord.otherPreferredMethodGuide = exclusion?.otherPreferredMethodGuide
      exclusionRecord.sameTypeRuleGuide = exclusion?.sameTypeRuleGuide
      exclusionRecord.scheduleFrequencyGuide = exclusion?.scheduleFrequencyGuide
      exclusionRecord.notAllowedIfEligibleForAnotherInterventionGuide =
        exclusion?.notAllowedIfEligibleForAnotherInterventionGuide
      exclusionRecord.literacyLevelGuide = exclusion?.literacyLevelGuide
      exclusionRecord.intervention = catalogue

      logger.info(
        "Upserted Exclusion record into Database for Intervention Catalogue Entry - " +
          "${catalogue.name}, id - ${catalogue.id}",
      )

      exclusionRecord
    } else {
      logger.info("Inserted Exclusion records into Database for Intervention Catalogue Entry - ${catalogue.name}, id - ${catalogue.id}")
      // If exclusion is null don't insert
      exclusion?.let {
        exclusionRepository.save(
          Exclusion(
            id = UUID.randomUUID(),
            minRemainingSentenceDurationGuide = exclusion.minRemainingSentenceGuide,
            remainingLicenseCommunityOrderGuide = exclusion.remainingLicenseCommunityOrderGuide,
            alcoholDrugProblemGuide = exclusion.alcoholDrugProblemGuide,
            mentalHealthProblemGuide = exclusion.mentalHealthProblemGuide,
            otherPreferredMethodGuide = exclusion.otherPreferredMethodGuide,
            sameTypeRuleGuide = exclusion.sameTypeRuleGuide,
            scheduleFrequencyGuide = exclusion.scheduleFrequencyGuide,
            notAllowedIfEligibleForAnotherInterventionGuide = exclusion.notAllowedIfEligibleForAnotherInterventionGuide,
            literacyLevelGuide = exclusion.literacyLevelGuide,
            intervention = catalogue,
          ),
        )
      }
    }
  }

  fun upsertPersonalEligibility(
    personalEligibility: PersonalEligibilityDefinition?,
    catalogue: InterventionCatalogue,
  ): PersonalEligibility {
    val personalEligibilityRecord: PersonalEligibility? = personalEligibilityRepository.findByIntervention(catalogue)

    when {
      personalEligibilityRecord != null -> {
        logger.info(
          "Retrieved Personal Eligibility record from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        personalEligibilityRecord.minAge = personalEligibility?.minAge?.toIntOrNull()
        personalEligibilityRecord.maxAge = personalEligibility?.maxAge?.toIntOrNull()
        personalEligibilityRecord.males = personalEligibility?.males == true
        personalEligibilityRecord.females = personalEligibility?.females == true
        personalEligibilityRecord.intervention = catalogue

        logger.info(
          "Upserted Personal Eligibility record into Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        return personalEligibilityRepository.save(personalEligibilityRecord)
      }

      else -> {
        logger.info(
          "Inserted Personal Eligibility records for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        return personalEligibilityRepository.save(
          PersonalEligibility(
            id = UUID.randomUUID(),
            minAge = personalEligibility?.minAge?.toIntOrNull(),
            maxAge = personalEligibility?.maxAge?.toIntOrNull(),
            males = personalEligibility?.males == true,
            females = personalEligibility?.females == true,
            intervention = catalogue,
          ),
        )
      }
    }
  }

  fun upsertPossibleOutcomes(
    possibleOutcomes: Array<String>,
    catalogue: InterventionCatalogue,
  ): MutableSet<PossibleOutcome> {
    val possibleOutcomesRecords: MutableList<PossibleOutcome> =
      possibleOutcomeRepository.findByIntervention(catalogue)?.toMutableList() ?: mutableListOf()

    fun createPossibleOutcomes() {
      possibleOutcomes.forEach { possibleOutcome ->
        possibleOutcomesRecords.add(
          PossibleOutcome(
            id = UUID.randomUUID(),
            outcome = possibleOutcome,
            intervention = catalogue,
          ),
        )
      }
    }

    when {
      possibleOutcomesRecords.isNotEmpty() -> {
        logger.info(
          "Retrieved and removed ${possibleOutcomesRecords.size} Possible Outcomes record from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        possibleOutcomeRepository.deleteAllByIntervention(catalogue)
        possibleOutcomesRecords.removeAll(possibleOutcomesRecords)
        createPossibleOutcomes()
      }
      else -> {
        createPossibleOutcomes()
      }
    }

    logger.info(
      "Inserted ${possibleOutcomesRecords.size} Possible Outcome record into Database for Intervention Catalogue Entry - " +
        "${catalogue.name}, id - ${catalogue.id}",
    )

    return possibleOutcomeRepository.saveAll(possibleOutcomesRecords).toMutableSet()
  }

  fun getRoshLevel(roshLevel: String?): RoshLevel? {
    val roshLevelUppercased: String? = roshLevel?.uppercase()
    val roshLevels: List<String> = RoshLevel.entries.map(RoshLevel::name)
    return if (roshLevels.contains(roshLevelUppercased)) {
      RoshLevel.entries[roshLevels.indexOf(roshLevelUppercased)]
    } else {
      null
    }
  }

  fun upsertRiskConsideration(
    riskConsideration: RiskConsiderationDefinition?,
    catalogue: InterventionCatalogue,
  ): RiskConsideration? {
    val riskConsiderationRecord: RiskConsideration? = riskConsiderationRepository.findByIntervention(catalogue)

    when {
      riskConsiderationRecord != null -> {
        logger.info(
          "Retrieved Risk Consideration record from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        riskConsiderationRecord.cnScoreGuide = riskConsideration?.cnScoreGuide
        riskConsiderationRecord.extremismRiskGuide = riskConsideration?.extremismRiskGuide
        riskConsiderationRecord.saraPartnerScoreGuide = riskConsideration?.saraPartnerScoreGuide
        riskConsiderationRecord.saraOtherScoreGuide = riskConsideration?.saraOtherScoreGuide
        riskConsiderationRecord.ospScoreGuide = riskConsideration?.ospScoreGuide
        riskConsiderationRecord.ospDcIccCombinationGuide = riskConsideration?.ospDcIccCombinationGuide
        riskConsiderationRecord.ogrsScoreGuide = riskConsideration?.ogrsScoreGuide
        riskConsiderationRecord.ovpGuide = riskConsideration?.ovpGuide
        riskConsiderationRecord.ogpGuide = riskConsideration?.ogpGuide
        riskConsiderationRecord.pnaGuide = riskConsideration?.pnaGuide
        riskConsiderationRecord.rsrGuide = riskConsideration?.rsrGuide
        riskConsiderationRecord.roshLevel = getRoshLevel(riskConsideration?.roshLevel)
        riskConsiderationRecord.intervention = catalogue

        logger.info(
          "Upserted Risk Consideration record into Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        return riskConsiderationRepository.save(riskConsiderationRecord)
      }

      else -> {
        logger.info(
          "Inserted Risk Consideration record into Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        return riskConsideration?.let {
          riskConsiderationRepository.save(
            RiskConsideration(
              id = UUID.randomUUID(),
              cnScoreGuide = riskConsideration.cnScoreGuide,
              extremismRiskGuide = riskConsideration.extremismRiskGuide,
              saraPartnerScoreGuide = riskConsideration.saraPartnerScoreGuide,
              saraOtherScoreGuide = riskConsideration.saraOtherScoreGuide,
              ospScoreGuide = riskConsideration.ospScoreGuide,
              ospDcIccCombinationGuide = riskConsideration.ospDcIccCombinationGuide,
              ogrsScoreGuide = riskConsideration.ogrsScoreGuide,
              ovpGuide = riskConsideration.ovpGuide,
              ogpGuide = riskConsideration.ogpGuide,
              pnaGuide = riskConsideration.pnaGuide,
              rsrGuide = riskConsideration.rsrGuide,
              roshLevel = getRoshLevel(riskConsideration.roshLevel),
              intervention = catalogue,
            ),
          )
        }
      }
    }
  }

  fun upsertSpecialEducationalNeed(
    specialEducationalNeed: SpecialEducationalNeedDefinition?,
    catalogue: InterventionCatalogue,
  ): SpecialEducationalNeed? {
    val specialEducationalNeedRecord: SpecialEducationalNeed? = specialEducationalNeedRepository.findByIntervention(catalogue)

    when {
      specialEducationalNeedRecord != null -> {
        logger.info(
          "Retrieved Special Educational Need record from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        specialEducationalNeedRecord.intervention = catalogue
        specialEducationalNeedRecord.literacyLevelGuide = specialEducationalNeed?.literacyLevelGuide
        specialEducationalNeedRecord.learningDisabilityCateredFor = specialEducationalNeed?.learningDisabilityCateredFor
        specialEducationalNeedRecord.equivalentNonLdcProgrammeGuide =
          specialEducationalNeed?.equivalentNonLdcProgrammeGuide

        logger.info(
          "Upserted Special Educational Need record into Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        return specialEducationalNeedRepository.save(specialEducationalNeedRecord)
      }

      else -> {
        logger.info(
          "Inserted Special Educational Need record into Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id - ${catalogue.id}",
        )

        return specialEducationalNeed?.let {
          specialEducationalNeedRepository.save(
            SpecialEducationalNeed(
              id = UUID.randomUUID(),
              literacyLevelGuide = specialEducationalNeed.literacyLevelGuide,
              learningDisabilityCateredFor = specialEducationalNeed.learningDisabilityCateredFor,
              equivalentNonLdcProgrammeGuide = specialEducationalNeed.equivalentNonLdcProgrammeGuide,
              intervention = catalogue,
            ),
          )
        }
      }
    }
  }
}
