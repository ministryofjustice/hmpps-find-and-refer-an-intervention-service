package uk.gov.justice.digital.hmpps.findandreferanintervention.jobs.scheduled.loadinterventions

import mu.KLogging
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.AuthUser
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeed
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.CriminogenicNeedRef
import uk.gov.justice.digital.hmpps.findandreferanintervention.jpa.entity.DeliveryLocation
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
        logger.info("Retrieved Intervention Catalogue Entry record - ${item.catalogue.name}, id = ${item.uuid}")

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
        logger.info("Inserting Intervention Catalogue Entry - ${item.catalogue.name}")
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

    logger.info("Inserted new Intervention Catalogue Entry record - ${catalogue.name}, id = $catalogueId")
    return interventionCatalogueRepository.save(newCatalogueRecord)
  }

  fun createInterventionCatalogue(
    item: InterventionCatalogueDefinition,
    catalogue: InterventionCatalogue,
  ): InterventionCatalogue {
    if (item.criminogenicNeed.toString().isNotEmpty()) {
      catalogue.criminogenicNeeds = upsertCriminogenicNeeds(item.criminogenicNeed, catalogue)
    }
    if (item.deliveryLocation?.isNotEmpty() == true) {
      catalogue.deliveryLocations = upsertDeliveryLocations(item.deliveryLocation, catalogue)
    }

    catalogue.deliveryMethods = upsertDeliveryMethods(item.deliveryMethod, catalogue)

    if (catalogue.deliveryMethods.isNotEmpty() && item.deliveryMethodSetting?.isNotEmpty() == true) {
      upsertDeliveryMethodSettings(item.deliveryMethodSetting, catalogue.deliveryMethods)
    }
    if (item.eligibleOffence?.isNotEmpty() == true) {
      catalogue.eligibleOffences = upsertEligibleOffences(item.eligibleOffence, catalogue)
    }
    if (item.enablingIntervention.toString().isNotEmpty()) {
      catalogue.enablingInterventions = upsertEnablingInterventions(item.enablingIntervention, catalogue)
    }
    if (item.excludedOffence?.isNotEmpty() == true) {
      catalogue.excludedOffences = upsertExcludedOffences(item.excludedOffence, catalogue)
    }
    if (item.exclusion.toString().isNotEmpty()) {
      catalogue.exclusion = upsertExclusion(item.exclusion, catalogue)
    }
    if (item.personalEligibility.toString().isNotEmpty()) {
      catalogue.personalEligibility = upsertPersonalEligibility(item.personalEligibility, catalogue)
    }
    if (item.possibleOutcome?.isNotEmpty() == true) {
      catalogue.possibleOutcomes = upsertPossibleOutcomes(item.possibleOutcome, catalogue)
    }
    if (item.riskConsideration.toString().isNotEmpty()) {
      catalogue.riskConsideration = upsertRiskConsideration(item.riskConsideration, catalogue)
    }
    if (item.specialEducationalNeed.toString().isNotEmpty()) {
      catalogue.specialEducationalNeeds = upsertSpecialEducationalNeed(item.specialEducationalNeed, catalogue)
    }

    logger.info("Finalised Intervention Catalogue Record for - ${catalogue.name}, id = ${catalogue.id}")
    return interventionCatalogueRepository.save(catalogue)
  }

  fun upsertCriminogenicNeeds(
    criminogenicNeeds: Array<String>,
    catalogue: InterventionCatalogue,
  ): MutableSet<CriminogenicNeed> {
    val criminogenicNeedRecords = criminogenicNeedRepository.findByIntervention(catalogue)?.toMutableList()

    when {
      criminogenicNeedRecords?.isNotEmpty() == true -> {
        logger.info("Retrieved ${criminogenicNeedRecords.size} Criminogenic Need records from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")
        return criminogenicNeedRecords.toMutableSet()
      }
      else -> {
        criminogenicNeeds.forEach { needReference ->
          val criminogenicReference = criminogenicNeedRefRepository.findByName(needReference)

          criminogenicNeedRecords?.add(
            CriminogenicNeed(
              id = UUID.randomUUID(),
              need = criminogenicReference ?: criminogenicNeedRefRepository.save(CriminogenicNeedRef(UUID.randomUUID(), needReference)),
              intervention = catalogue,
            ),
          )
        }

        return if (criminogenicNeedRecords?.isNotEmpty() == true) {
          logger.info("Inserted ${criminogenicNeedRecords.size} Criminogenic Need records from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")
          return criminogenicNeedRepository.saveAll(criminogenicNeedRecords).toMutableSet()
        } else {
          mutableSetOf()
        }
      }
    }
  }

  fun upsertDeliveryLocations(
    deliveryLocations: Array<DeliveryLocationDefinition>,
    catalogue: InterventionCatalogue,
  ): MutableSet<DeliveryLocation> {
    val deliveryLocationRecords = deliveryLocationRepository.findByIntervention(catalogue)?.toMutableList()

    when {
      deliveryLocationRecords?.isNotEmpty() == true -> {
        logger.info("Retrieved ${deliveryLocationRecords.size} Delivery Location records from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")
        return deliveryLocationRecords.toMutableSet()
      }
      else -> {
        for (deliveryLocation in deliveryLocations) {
          for (pdu in deliveryLocation.pduRefs) {
            val pduRef = pduRefRepository.findByName(pdu)

            if (pduRef != null) {
              val newDeliveryLocation = deliveryLocationRepository.save(
                DeliveryLocation(
                  id = UUID.randomUUID(),
                  providerName = deliveryLocation.providerName,
                  contact = deliveryLocation.contact,
                  pduRef = pduRef,
                  intervention = catalogue,
                ),
              )

              deliveryLocationRecords?.add(newDeliveryLocation)
            } else {
              throw RuntimeException("PDU for ${deliveryLocation.providerName} was not found")
            }
          }
        }

        return if (deliveryLocationRecords?.isNotEmpty() == true) {
          logger.info(
            "Inserted ${deliveryLocationRecords.size} Delivery Location records from Database for Intervention Catalogue Entry - " +
              "${catalogue.name}, id = ${catalogue.id}",
          )
          return deliveryLocationRecords.toMutableSet()
        } else {
          mutableSetOf()
        }
      }
    }
  }

  fun upsertDeliveryMethods(
    deliveryMethods: Array<DeliveryMethodDefinition>?,
    catalogue: InterventionCatalogue,
  ): MutableSet<DeliveryMethod> {
    val deliveryMethodRecords = deliveryMethodRepository.findByIntervention(catalogue)?.toMutableList()

    when {
      deliveryMethodRecords?.isNotEmpty() == true -> {
        logger.info(
          "Retrieved ${deliveryMethodRecords.size} Delivery Method records from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id = ${catalogue.id}",
        )
        return deliveryMethodRecords.toMutableSet()
      }
      else -> {
        if (deliveryMethods?.isNotEmpty() == true) {
          for (deliveryMethod in deliveryMethods) {
            deliveryMethodRecords?.add(
              DeliveryMethod(
                id = UUID.randomUUID(),
                attendanceType = deliveryMethod.attendanceType,
                deliveryFormat = deliveryMethod.deliveryFormat,
                deliveryMethodDescription = deliveryMethod.deliveryMethodDescription,
                intervention = catalogue,
              ),
            )
          }
        } else {
          deliveryMethodRecords?.add(
            DeliveryMethod(
              id = UUID.randomUUID(),
              attendanceType = null,
              deliveryFormat = null,
              deliveryMethodDescription = null,
              intervention = catalogue,
            ),
          )
        }

        return if (deliveryMethodRecords?.isNotEmpty() == true) {
          logger.info(
            "Inserted ${deliveryMethodRecords.size} Delivery Method records from Database for Intervention Catalogue Entry " +
              "- ${catalogue.name}, id = ${catalogue.id}",
          )
          return deliveryMethodRepository.saveAll(deliveryMethodRecords).toMutableSet()
        } else {
          mutableSetOf()
        }
      }
    }
  }

  fun hasDeliveryMethodSetting(settingName: String?): SettingType? {
    val settingNameUppercased = settingName?.uppercase()
    val settingTypes = SettingType.entries.map(SettingType::name)
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
    deliveryMethods: MutableSet<DeliveryMethod>,
  ): MutableSet<DeliveryMethodSetting> {
    var deliveryMethodSettingRecords = mutableListOf<DeliveryMethodSetting>()

    deliveryMethods.forEach { deliveryMethod ->
      deliveryMethodSettingRecords =
        deliveryMethodSettingRepository.findByDeliveryMethodId(deliveryMethod.id)?.toMutableList() ?: mutableListOf()
    }

    when {
      deliveryMethodSettingRecords.isNotEmpty() -> {
        logger.info(
          "Retrieved ${deliveryMethodSettingRecords.size} Delivery Method Setting records",
        )
        return deliveryMethodSettingRecords.toMutableSet()
      }
      else -> {
        for (deliveryMethod in deliveryMethods) {
          for (deliveryMethodSetting in deliveryMethodSettings) {
            val settingType = hasDeliveryMethodSetting(deliveryMethodSetting)

            when {
              settingType != null -> {
                deliveryMethodSettingRecords.add(
                  DeliveryMethodSetting(id = UUID.randomUUID(), deliveryMethodId = deliveryMethod.id, setting = settingType),
                )
              }
              else -> {
                logger.info(
                  "Unable to create Delivery Method Setting record for Id - ${deliveryMethod.id}, as Setting Type is null or invalid",
                )
              }
            }
          }
        }
        deliveryMethodSettingRepository.saveAll(deliveryMethodSettingRecords)
        logger.info("Inserted ${deliveryMethodSettingRecords.size} Delivery Method Setting records")
        return deliveryMethodSettingRecords.toMutableSet()
      }
    }
  }

  fun upsertEligibleOffences(
    eligibleOffences: Array<EligibleOffenceDefinition>,
    catalogue: InterventionCatalogue,
  ): MutableSet<EligibleOffence> {
    val eligibleOffenceRecords = eligibleOffenceRepository.findByIntervention(catalogue)?.toMutableList()

    when {
      eligibleOffenceRecords?.isNotEmpty() == true -> {
        logger.info("Retrieved ${eligibleOffenceRecords.size} Eligible Offence records from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")
        return eligibleOffenceRecords.toMutableSet()
      }
      else -> {
        for (eligibleOffence in eligibleOffences) {
          val offenceType = offenceTypeRefRepository.findByName(eligibleOffence.offenceTypeId)

          eligibleOffenceRecords?.add(
            EligibleOffence(
              id = UUID.randomUUID(),
              offenceType = offenceType ?: offenceTypeRefRepository.save(OffenceTypeRef(UUID.randomUUID(), eligibleOffence.offenceTypeId)),
              victimType = eligibleOffence.victimType,
              motivation = eligibleOffence.motivation,
              intervention = catalogue,
            ),
          )
        }

        return if (eligibleOffenceRecords?.isNotEmpty() == true) {
          logger.info("Inserted ${eligibleOffenceRecords.size} Eligible Offence records for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")
          return eligibleOffenceRepository.saveAll(eligibleOffenceRecords).toMutableSet()
        } else {
          mutableSetOf()
        }
      }
    }
  }

  fun upsertEnablingInterventions(
    details: String?,
    catalogue: InterventionCatalogue,
  ): MutableSet<EnablingIntervention> {
    val enablingInterventionRecords = enablingInterventionRepository.findByIntervention(catalogue)?.toMutableList()

    when {
      enablingInterventionRecords?.isNotEmpty() == true -> {
        logger.info(
          "Retrieved ${enablingInterventionRecords.size} Enabling Intervention records from Database for Intervention Catalogue Entry - " +
            "${catalogue.name}, id = ${catalogue.id}",
        )

        return enablingInterventionRecords.toMutableSet()
      }
      else -> {
        details?.split(",")?.forEach { detail ->
          enablingInterventionRecords?.add(
            EnablingIntervention(id = UUID.randomUUID(), enablingInterventionDetail = detail, intervention = catalogue),
          )
        }

        return if (enablingInterventionRecords?.isNotEmpty() == true) {
          logger.info("Inserted ${enablingInterventionRecords.size} Enabling Intervention records for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")
          return enablingInterventionRepository.saveAll(enablingInterventionRecords).toMutableSet()
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
    val excludedOffenceRecords = excludedOffenceRepository.findByIntervention(catalogue)?.toMutableList()

    when {
      excludedOffenceRecords?.isNotEmpty() == true -> {
        logger.info("Retrieved ${excludedOffenceRecords.size} Excluded Offence records from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")
        return excludedOffenceRecords.toMutableSet()
      }
      else -> {
        for (excludedOffence in excludedOffences) {
          val offenceType = offenceTypeRefRepository.findByName(excludedOffence.offenceTypeId)

          excludedOffenceRecords?.add(
            ExcludedOffence(
              id = UUID.randomUUID(),
              offenceType = offenceType ?: offenceTypeRefRepository.save(OffenceTypeRef(UUID.randomUUID(), excludedOffence.offenceTypeId)),
              victimType = excludedOffence.victimType,
              motivation = excludedOffence.motivation,
              intervention = catalogue,
            ),
          )
        }

        return if (excludedOffenceRecords?.isNotEmpty() == true) {
          logger.info("Inserted ${excludedOffenceRecords.size} Excluded Offence records for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")
          return excludedOffenceRepository.saveAll(excludedOffenceRecords).toMutableSet()
        } else {
          mutableSetOf()
        }
      }
    }
  }

  fun upsertExclusion(
    exclusion: ExclusionDefinition?,
    catalogue: InterventionCatalogue,
  ): Exclusion {
    val exclusionRecord = exclusionRepository.findByIntervention(catalogue)

    when {
      exclusionRecord != null -> {
        logger.info("Retrieved Exclusion record from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")

        exclusionRecord.minRemainingSentenceDurationGuide = exclusion?.minRemaingSentenceGuide
        exclusionRecord.remainingLicenseCommunityOrderGuide = exclusion?.remainingLicenseCommunityOrderGuide
        exclusionRecord.alcoholDrugProblemGuide = exclusion?.alcoholDrugProblemGuide
        exclusionRecord.mentalHealthProblemGuide = exclusion?.mentalHealthProblemGuide
        exclusionRecord.otherPreferredMethodGuide = exclusion?.otherPreferredMethodGuide
        exclusionRecord.sameTypeRuleGuide = exclusion?.sameTypeRuleGuide
        exclusionRecord.scheduleFrequencyGuide = exclusion?.scheduleRequencyGuide
        exclusionRecord.intervention = catalogue

        logger.info(
          "Exclusion record have now been upserted for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}",
        )

        return exclusionRecord
      }
      else -> {
        logger.info("Inserted Exclusion records for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}")

        return exclusionRepository.save(
          Exclusion(
            id = UUID.randomUUID(),
            minRemainingSentenceDurationGuide = exclusion?.minRemaingSentenceGuide,
            remainingLicenseCommunityOrderGuide = exclusion?.remainingLicenseCommunityOrderGuide,
            alcoholDrugProblemGuide = exclusion?.alcoholDrugProblemGuide,
            mentalHealthProblemGuide = exclusion?.mentalHealthProblemGuide,
            otherPreferredMethodGuide = exclusion?.otherPreferredMethodGuide,
            sameTypeRuleGuide = exclusion?.sameTypeRuleGuide,
            scheduleFrequencyGuide = exclusion?.scheduleRequencyGuide,
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
    val personalEligibilityRecord = personalEligibilityRepository.findByIntervention(catalogue)

    when {
      personalEligibilityRecord != null -> {
        logger.info(
          "Retrieved Personal Eligibility record from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}",
        )

        personalEligibilityRecord.minAge = personalEligibility?.minAge?.toIntOrNull()
        personalEligibilityRecord.maxAge = personalEligibility?.maxAge?.toIntOrNull()
        personalEligibilityRecord.males = personalEligibility?.males == true
        personalEligibilityRecord.females = personalEligibility?.females == true
        personalEligibilityRecord.intervention = catalogue

        logger.info(
          "Personal Eligibility record have now been upserted for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}",
        )

        return personalEligibilityRecord
      }
      else -> {
        logger.info("Inserted Personal Eligibility records for Intervention Catalogue Entry - ${catalogue.name}")

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
    val possibleOutcomeList = mutableListOf<PossibleOutcome>()
    val possibleOutcomesRecord = possibleOutcomeRepository.findByIntervention(catalogue)

    when {
      possibleOutcomesRecord != null -> {
        logger.info(
          "Retrieved Possible Outcome record from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}",
        )

        possibleOutcomesRecord.outcome = possibleOutcomes.toString()
        possibleOutcomeList.add(possibleOutcomesRecord)

        logger.info(
          "Possible Outcomes record has now been upserted for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}",
        )

        return possibleOutcomeList.toMutableSet()
      }
      else -> {
        for (possibleOutcome in possibleOutcomes) {
          possibleOutcomeList.add(
            PossibleOutcome(
              id = UUID.randomUUID(),
              outcome = possibleOutcome,
              intervention = catalogue,
            ),
          )
        }

        logger.info("Inserted ${possibleOutcomeList.size} Possible Outcome record for Intervention Catalogue Entry - ${catalogue.name}")
        return possibleOutcomeRepository.saveAll(possibleOutcomeList).toMutableSet()
      }
    }
  }

  fun hasRoshLevel(roshLevel: String?): RoshLevel? {
    val roshLevelUppercased = roshLevel?.uppercase()
    val roshLevels = RoshLevel.entries.map(RoshLevel::name)
    return if (roshLevels.contains(roshLevelUppercased)) {
      RoshLevel.entries[roshLevels.indexOf(roshLevelUppercased)]
    } else {
      null
    }
  }

  fun upsertRiskConsideration(
    riskConsideration: RiskConsiderationDefinition?,
    catalogue: InterventionCatalogue,
  ): RiskConsideration {
    val riskConsiderationRecord = riskConsiderationRepository.findByIntervention(catalogue)

    when {
      riskConsiderationRecord != null -> {
        logger.info(
          "Retrieved Risk Consideration record from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}",
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
        riskConsiderationRecord.roshLevel = hasRoshLevel(riskConsideration?.roshLevel)
        riskConsiderationRecord.intervention = catalogue

        logger.info(
          "Risk Consideration record have now been upserted for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}",
        )

        return riskConsiderationRecord
      }
      else -> {
        logger.info("Inserted Risk Consideration record for Intervention Catalogue Entry - ${catalogue.name}")

        return riskConsiderationRepository.save(
          RiskConsideration(
            id = UUID.randomUUID(),
            cnScoreGuide = riskConsideration?.cnScoreGuide,
            extremismRiskGuide = riskConsideration?.extremismRiskGuide,
            saraPartnerScoreGuide = riskConsideration?.saraPartnerScoreGuide,
            saraOtherScoreGuide = riskConsideration?.saraOtherScoreGuide,
            ospScoreGuide = riskConsideration?.ospScoreGuide,
            ospDcIccCombinationGuide = riskConsideration?.ospDcIccCombinationGuide,
            ogrsScoreGuide = riskConsideration?.ogrsScoreGuide,
            ovpGuide = riskConsideration?.ovpGuide,
            ogpGuide = riskConsideration?.ogpGuide,
            pnaGuide = riskConsideration?.pnaGuide,
            rsrGuide = riskConsideration?.rsrGuide,
            roshLevel = hasRoshLevel(riskConsideration?.roshLevel),
            intervention = catalogue,
          ),
        )
      }
    }
  }

  fun upsertSpecialEducationalNeed(
    specialEducationalNeed: SpecialEducationalNeedDefinition?,
    catalogue: InterventionCatalogue,
  ): SpecialEducationalNeed {
    val specialEducationalNeedRecord = specialEducationalNeedRepository.findByIntervention(catalogue)

    when {
      specialEducationalNeedRecord != null -> {
        logger.info(
          "Retrieved Special Educational Need record from Database for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}",
        )
        specialEducationalNeedRecord.intervention = catalogue
        specialEducationalNeedRecord.literacyLevelGuide = specialEducationalNeed?.literacyLevelGuide
        specialEducationalNeedRecord.learningDisabilityCateredFor = specialEducationalNeed?.learningDisabilityCateredFor
        specialEducationalNeedRecord.equivalentNonLdcProgrammeGuide = specialEducationalNeed?.equivalentNonLdcProgrammeGuide

        logger.info(
          "Special Educational Need record have now been upserted for Intervention Catalogue Entry - ${catalogue.name}, id = ${catalogue.id}",
        )

        return specialEducationalNeedRecord
      }
      else -> {
        logger.info("Inserted Special Educational Need record for Intervention Catalogue Entry - ${catalogue.name}")

        return specialEducationalNeedRepository.save(
          SpecialEducationalNeed(
            id = UUID.randomUUID(),
            literacyLevelGuide = specialEducationalNeed?.literacyLevelGuide,
            learningDisabilityCateredFor = specialEducationalNeed?.learningDisabilityCateredFor,
            equivalentNonLdcProgrammeGuide = specialEducationalNeed?.equivalentNonLdcProgrammeGuide,
            intervention = catalogue,
          ),
        )
      }
    }
  }
}
