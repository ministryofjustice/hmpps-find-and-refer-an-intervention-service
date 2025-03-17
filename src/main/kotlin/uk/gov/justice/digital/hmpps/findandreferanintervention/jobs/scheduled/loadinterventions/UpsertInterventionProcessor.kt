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
      else -> logger.info("Creating Intervention Catalogue Record - ${item.catalogue.name}")
    }

    return createInterventionCatalogue(item)
  }

  fun checkInterventionType(item: InterventionCatalogueDefinition): Boolean {
    val interventionTypes = InterventionType.entries.map(InterventionType::name)
    return interventionTypes.contains(item.catalogue.interventionType.uppercase())
  }

  fun createInterventionCatalogue(item: InterventionCatalogueDefinition): InterventionCatalogue {
    val catalogue = insertInterventionCatalogueEntry(item.catalogue, item.uuid)

    if (item.criminogenicNeed.toString().isNotEmpty()) {
      catalogue.criminogenicNeeds = insertCriminogenicNeeds(item.criminogenicNeed, catalogue)
    }
    if (item.deliveryLocation?.isNotEmpty() == true) {
      catalogue.deliveryLocations = insertDeliveryLocations(item.deliveryLocation, catalogue)
    }
    if (item.deliveryMethod?.isNotEmpty() == true) {
      catalogue.deliveryMethods = insertDeliveryMethods(item.deliveryMethod, catalogue)
    }
    if (item.deliveryMethod?.isNotEmpty() == true && item.deliveryMethodSetting?.isNotEmpty() == true) {
      insertDeliveryMethodSettings(item.deliveryMethodSetting, catalogue.deliveryMethods)
    }
    if (item.eligibleOffence?.isNotEmpty() == true) {
      catalogue.eligibleOffences = insertEligibleOffences(item.eligibleOffence, catalogue)
    }
    if (item.enablingIntervention.toString().isNotEmpty()) {
      catalogue.enablingInterventions = insertEnablingInterventions(item.enablingIntervention, catalogue)
    }
    if (item.excludedOffence?.isNotEmpty() == true) {
      catalogue.excludedOffences = insertExcludedOffences(item.excludedOffence, catalogue)
    }
    if (item.exclusion.toString().isNotEmpty()) {
      catalogue.exclusion = insertExclusion(item.exclusion, catalogue)
    }
    if (item.personalEligibility.toString().isNotEmpty()) {
      catalogue.personalEligibility = insertPersonalEligibility(item.personalEligibility, catalogue)
    }
    if (item.possibleOutcome?.isNotEmpty() == true) {
      catalogue.possibleOutcomes = insertPossibleOutcomes(item.possibleOutcome, catalogue)
    }
    if (item.riskConsideration.toString().isNotEmpty()) {
      catalogue.riskConsideration = insertRiskConsideration(item.riskConsideration, catalogue)
    }
    if (item.specialEducationalNeed.toString().isNotEmpty()) {
      catalogue.specialEducationalNeeds = insertSpecialEducationalNeed(item.specialEducationalNeed, catalogue)
    }

    logger.info("Intervention Catalogue Record Inserted for - ${catalogue.name}")
    return interventionCatalogueRepository.save(catalogue)
  }

  fun insertInterventionCatalogueEntry(catalogue: InterventionCatalogueEntryDefinition, catalogueId: UUID): InterventionCatalogue {
    val catalogueRecord = InterventionCatalogue(
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

    return interventionCatalogueRepository.save(catalogueRecord)
  }

  fun insertCriminogenicNeeds(
    criminogenicNeeds: Array<String>,
    catalogue: InterventionCatalogue,
  ): MutableSet<CriminogenicNeed> {
    val criminogenicNeedList = mutableListOf<CriminogenicNeed>()

    criminogenicNeeds.forEach { needReference ->
      val criminogenicReference = criminogenicNeedRefRepository.findByName(needReference)

      criminogenicNeedList.add(
        CriminogenicNeed(
          id = UUID.randomUUID(),
          need = criminogenicReference ?: criminogenicNeedRefRepository.save(CriminogenicNeedRef(UUID.randomUUID(), needReference)),
          intervention = catalogue,
        ),
      )
    }

    return if (criminogenicNeedList.size > 0) {
      logger.info("Inserted Criminogenic Need records for Intervention Catalogue Entry - ${catalogue.name}")
      return criminogenicNeedRepository.saveAll(criminogenicNeedList).toMutableSet()
    } else {
      mutableSetOf()
    }
  }

  fun insertDeliveryLocations(
    deliveryLocations: Array<DeliveryLocationDefinition>,
    catalogue: InterventionCatalogue,
  ): MutableSet<DeliveryLocation> {
    val deliveryLocationList = mutableListOf<DeliveryLocation>()

    for (deliveryLocation in deliveryLocations) {
      for (pdu in deliveryLocation.pduRefs) {
        val pduRef = pduRefRepository.findByName(pdu)

        if (pduRef != null) {
          val insertedDeliveryLocation = deliveryLocationRepository.save(
            DeliveryLocation(
              id = UUID.randomUUID(),
              providerName = deliveryLocation.providerName,
              contact = deliveryLocation.contact,
              pduRef = pduRef,
              intervention = catalogue,
            ),
          )

          deliveryLocationList.add(insertedDeliveryLocation)
        } else {
          throw RuntimeException("PDU for ${deliveryLocation.providerName} was not found")
        }
      }
    }

    logger.info("Inserted Delivery Location records for Intervention Catalogue Entry - ${catalogue.name}")
    return deliveryLocationList.toMutableSet()
  }

  fun insertDeliveryMethods(
    deliveryMethods: Array<DeliveryMethodDefinition>,
    catalogue: InterventionCatalogue,
  ): MutableSet<DeliveryMethod> {
    val deliveryMethodList = mutableListOf<DeliveryMethod>()

    for (deliveryMethod in deliveryMethods) {
      deliveryMethodList.add(
        DeliveryMethod(
          id = UUID.randomUUID(),
          attendanceType = deliveryMethod.attendanceType,
          deliveryFormat = deliveryMethod.deliveryFormat,
          deliveryMethodDescription = deliveryMethod.deliveryMethodDescription,
          intervention = catalogue,
        ),
      )
    }

    logger.info("Inserted Delivery Methods records for Intervention Catalogue Entry - ${catalogue.name}")
    return deliveryMethodRepository.saveAll(deliveryMethodList).toMutableSet()
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

  fun insertDeliveryMethodSettings(
    deliveryMethodSettings: Array<String>,
    deliveryMethods: MutableSet<DeliveryMethod>,
  ): MutableSet<DeliveryMethodSetting> {
    val deliveryMethodSettingList = mutableListOf<DeliveryMethodSetting>()

    for (deliveryMethod in deliveryMethods) {
      for (deliveryMethodSetting in deliveryMethodSettings) {
        val settingType = hasDeliveryMethodSetting(deliveryMethodSetting)

        when {
          settingType != null -> {
            deliveryMethodSettingList.add(
              DeliveryMethodSetting(
                id = UUID.randomUUID(),
                deliveryMethodId = deliveryMethod.id,
                setting = settingType,
              ),
            )
            logger.info("Inserted Delivery Method Setting record for Delivery Method Id - ${deliveryMethod.id}")
          }
          else -> {
            logger.info(
              "Unable to create Delivery Method Setting record for Id - ${deliveryMethod.id}, as Setting Type is null or invalid",
            )
          }
        }
      }
    }

    return deliveryMethodSettingRepository.saveAll(deliveryMethodSettingList).toMutableSet()
  }

  fun insertEligibleOffences(
    eligibleOffences: Array<EligibleOffenceDefinition>,
    catalogue: InterventionCatalogue,
  ): MutableSet<EligibleOffence> {
    val eligibleOffenceList = mutableListOf<EligibleOffence>()

    for (eligibleOffence in eligibleOffences) {
      val offenceType = offenceTypeRefRepository.findByName(eligibleOffence.offenceTypeId)

      eligibleOffenceList.add(
        EligibleOffence(
          id = UUID.randomUUID(),
          offenceType = offenceType ?: offenceTypeRefRepository.save(OffenceTypeRef(UUID.randomUUID(), eligibleOffence.offenceTypeId)),
          victimType = eligibleOffence.victimType,
          motivation = eligibleOffence.motivation,
          intervention = catalogue,
        ),
      )
    }

    return if (eligibleOffenceList.size > 0) {
      logger.info("Inserted Eligible Offence records for Intervention Catalogue Entry - ${catalogue.name}")
      return eligibleOffenceRepository.saveAll(eligibleOffenceList).toMutableSet()
    } else {
      mutableSetOf()
    }
  }

  fun insertEnablingInterventions(
    details: String?,
    catalogue: InterventionCatalogue,
  ): MutableSet<EnablingIntervention> {
    val detailsList = details?.split(",")
    val enablingInterventionList = mutableListOf<EnablingIntervention>()

    detailsList?.forEach { detail ->
      enablingInterventionList.add(
        EnablingIntervention(
          id = UUID.randomUUID(),
          enablingInterventionDetail = detail,
          intervention = catalogue,
        ),
      )
    }

    logger.info("Inserted Enabling Intervention record for Intervention Catalogue Entry - ${catalogue.name}")
    return enablingInterventionRepository.saveAll(enablingInterventionList).toMutableSet()
  }

  fun insertExcludedOffences(
    excludedOffences: Array<ExcludedOffencesDefinition>,
    catalogue: InterventionCatalogue,
  ): MutableSet<ExcludedOffence> {
    val excludedOffenceList = mutableListOf<ExcludedOffence>()

    for (excludedOffence in excludedOffences) {
      val offenceType = offenceTypeRefRepository.findByName(excludedOffence.offenceTypeId)

      excludedOffenceList.add(
        ExcludedOffence(
          id = UUID.randomUUID(),
          offenceType = offenceType ?: offenceTypeRefRepository.save(OffenceTypeRef(UUID.randomUUID(), excludedOffence.offenceTypeId)),
          victimType = excludedOffence.victimType,
          motivation = excludedOffence.motivation,
          intervention = catalogue,
        ),
      )
    }

    return if (excludedOffenceList.size > 0) {
      logger.info("Inserted Excluded Offence records for Intervention Catalogue Entry - ${catalogue.name}")
      return excludedOffenceRepository.saveAll(excludedOffenceList).toMutableSet()
    } else {
      mutableSetOf()
    }
  }

  fun insertExclusion(
    exclusion: ExclusionDefinition?,
    catalogue: InterventionCatalogue,
  ): Exclusion {
    val exclusionRecord = Exclusion(
      id = UUID.randomUUID(),
      minRemainingSentenceDurationGuide = exclusion?.minRemaingSentenceGuide,
      remainingLicenseCommunityOrderGuide = exclusion?.remainingLicenseCommunityOrderGuide,
      alcoholDrugProblemGuide = exclusion?.alcoholDrugProblemGuide,
      mentalHealthProblemGuide = exclusion?.mentalHealthProblemGuide,
      otherPreferredMethodGuide = exclusion?.otherPreferredMethodGuide,
      sameTypeRuleGuide = exclusion?.sameTypeRuleGuide,
      scheduleFrequencyGuide = exclusion?.scheduleRequencyGuide,
      intervention = catalogue,
    )

    logger.info("Inserted Exclusion records for Intervention Catalogue Entry - ${catalogue.name}")
    return exclusionRepository.save(exclusionRecord)
  }

  fun insertPersonalEligibility(
    personalEligibility: PersonalEligibilityDefinition?,
    catalogue: InterventionCatalogue,
  ): PersonalEligibility {
    val personalEligibilityRecord = PersonalEligibility(
      id = UUID.randomUUID(),
      minAge = personalEligibility?.minAge?.toIntOrNull(),
      maxAge = personalEligibility?.maxAge?.toIntOrNull(),
      males = personalEligibility?.males == true,
      females = personalEligibility?.females == true,
      intervention = catalogue,
    )

    logger.info("Inserted Personal Eligibility records for Intervention Catalogue Entry - ${catalogue.name}")
    return personalEligibilityRepository.save(personalEligibilityRecord)
  }

  fun insertPossibleOutcomes(
    possibleOutcomes: Array<String>,
    catalogue: InterventionCatalogue,
  ): MutableSet<PossibleOutcome> {
    val possibleOutcomeList = mutableListOf<PossibleOutcome>()

    for (possibleOutcome in possibleOutcomes) {
      possibleOutcomeList.add(
        PossibleOutcome(id = UUID.randomUUID(), outcome = possibleOutcome),
      )
    }

    logger.info("Inserted Possible Outcome records for Intervention Catalogue Entry - ${catalogue.name}")
    return possibleOutcomeRepository.saveAll(possibleOutcomeList).toMutableSet()
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

  fun insertRiskConsideration(
    riskConsideration: RiskConsiderationDefinition?,
    catalogue: InterventionCatalogue,
  ): RiskConsideration {
    val riskConsiderationRecord = RiskConsideration(
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
    )

    logger.info("Inserted Risk Consideration records for Intervention Catalogue Entry - ${catalogue.name}")
    return riskConsiderationRepository.save(riskConsiderationRecord)
  }

  fun insertSpecialEducationalNeed(
    specialEducationalNeed: SpecialEducationalNeedDefinition?,
    catalogue: InterventionCatalogue,
  ): SpecialEducationalNeed {
    val specialEducationalNeedsRecord = SpecialEducationalNeed(
      id = UUID.randomUUID(),
      literacyLevelGuide = specialEducationalNeed?.literacyLevelGuide,
      learningDisabilityCateredFor = specialEducationalNeed?.learningDisabilityCateredFor,
      equivalentNonLdcProgrammeGuide = specialEducationalNeed?.equivalentNonLdcProgrammeGuide,
      intervention = catalogue,
    )

    logger.info("Inserted Special Educational Needs records for Intervention Catalogue Entry - ${catalogue.name}")
    return specialEducationalNeedRepository.save(specialEducationalNeedsRecord)
  }
}
