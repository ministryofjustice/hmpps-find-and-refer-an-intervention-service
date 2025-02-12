COMMENT ON TABLE auth_user IS 'details about the user from hmpps-auth';
COMMENT ON COLUMN auth_user.id IS 'the user ID';
COMMENT ON COLUMN auth_user.auth_source IS 'where the user has come from';
COMMENT ON COLUMN auth_user.user_name IS 'the username';
COMMENT ON COLUMN auth_user.deleted IS 'soft deleted flag';

COMMENT ON TABLE service_category IS '**reference data** intervention service categories, which relate to service user needs';
COMMENT ON COLUMN service_category.id IS 'service-owned unique identifier';
COMMENT ON COLUMN service_category.created IS 'when the record was added';
COMMENT ON COLUMN service_category.name IS 'intervention service category';

COMMENT ON TABLE complexity_level IS '**reference data** complexity levels for each intervention service category';
COMMENT ON COLUMN complexity_level.id IS 'service-owned unique identifier';
COMMENT ON COLUMN complexity_level.service_category_id IS 'the ID of the intervention service category it belongs to';
COMMENT ON COLUMN complexity_level.title IS 'complexity level of needs, usually: low, medium, high';
COMMENT ON COLUMN complexity_level.description IS 'rationale for the complexity level';

COMMENT ON TABLE delivery_method IS '**reference data** applicable delivery method for each intervention';
COMMENT ON COLUMN delivery_method.id IS 'service-owned unique identifier';
COMMENT ON COLUMN delivery_method.intervention_id IS 'the ID of the intervention delivery method belongs to';
COMMENT ON COLUMN delivery_method.delivery_method_description IS 'describes what the delivery method is intended to be';
COMMENT ON COLUMN delivery_method.attendance_type IS 'the type of attendance such as group or in person basis';
COMMENT ON COLUMN delivery_method.delivery_format IS 'description of how the session is delivered';

COMMENT ON TABLE delivery_method_setting IS '**reference data** applicable delivery method setting for each intervention';
COMMENT ON COLUMN delivery_method_setting.setting IS 'type of setting in which this delivery method takes place';

COMMENT ON TABLE dummy_table IS 'dummy table only present for initial stages of development';
COMMENT ON COLUMN dummy_table.dummy_id IS 'service-owned unique identifier';
COMMENT ON COLUMN dummy_table.dummy_date IS 'date the dummy row was inserted';
COMMENT ON COLUMN dummy_table.dummy_description IS 'some simple sample text';

COMMENT ON TABLE desired_outcome IS '**reference data** desired outcomes available for each intervention service category';
COMMENT ON COLUMN desired_outcome.id IS 'service-owned unique identifier';
COMMENT ON COLUMN desired_outcome.service_category_id IS 'the ID of the intervention service category it belongs to';
COMMENT ON COLUMN desired_outcome.description IS 'describes what the outcome should be for the service user';

COMMENT ON TABLE dynamic_framework_contract IS 'stores the scope of the commissioned rehabilitative services contracts';
COMMENT ON COLUMN dynamic_framework_contract.id IS 'service-owned unique identifier';
COMMENT ON COLUMN dynamic_framework_contract.prime_provider_id IS 'awarded for a (prime) provider';
COMMENT ON COLUMN dynamic_framework_contract.start_date IS 'when delivery can start';
COMMENT ON COLUMN dynamic_framework_contract.end_date IS 'when delivery must end';
COMMENT ON COLUMN dynamic_framework_contract.nps_region_id IS 'awarded to deliver in either an NPS or PCC region';
COMMENT ON COLUMN dynamic_framework_contract.pcc_region_id IS 'awarded to deliver in either an NPS or PCC region';
COMMENT ON COLUMN dynamic_framework_contract.allows_female IS 'whether female service users can attend';
COMMENT ON COLUMN dynamic_framework_contract.allows_male IS 'whether can male service users can attend';
COMMENT ON COLUMN dynamic_framework_contract.minimum_age IS 'the minimum age of attending service users';
COMMENT ON COLUMN dynamic_framework_contract.maximum_age IS 'the maximum age of attending service users';

COMMENT ON TABLE eligible_ethnicity IS 'stores the details of ethnicity for interventions that are only available to specific groups';
COMMENT ON COLUMN eligible_ethnicity.id IS 'service-owned unique identifier';
COMMENT ON COLUMN eligible_ethnicity.ethnicity_id IS 'related value from the ethnicity reference table';
COMMENT ON COLUMN eligible_ethnicity.personal_eligibility_id IS 'identifier of the parent personal eligibility record';

COMMENT ON TABLE eligible_offence IS 'stores the details of offences for interventions that are only available to offenders that have committed certain offences';
COMMENT ON COLUMN eligible_offence.id IS 'service-owned unique identifier';
COMMENT ON COLUMN eligible_offence.intervention_id IS 'identifier of the applicable intervention for this offence';
COMMENT ON COLUMN eligible_offence.motivation IS 'service users motivation to commit the offence';
COMMENT ON COLUMN eligible_offence.offence_type_id IS 'related value from the offence types table';
COMMENT ON COLUMN eligible_offence.victim_type IS 'the type of victim that the offence affected';

COMMENT ON TABLE nps_region IS '**reference data** National Probation Service (NPS) region details';
COMMENT ON COLUMN nps_region.id IS 'the ID of the NPS region unique identifier';
COMMENT ON COLUMN nps_region.name IS 'NPS region name';

COMMENT ON TABLE pcc_region IS '**reference data** Police and Crime Commissioner (PCC) region details';
COMMENT ON COLUMN pcc_region.id IS 'PCC region unique identifier';
COMMENT ON COLUMN pcc_region.name IS 'PCC region name';

COMMENT ON TABLE service_provider IS 'service provider details';
COMMENT ON COLUMN service_provider.id IS 'service provider unique identifier, used in hmpps-auth as group code';
COMMENT ON COLUMN service_provider.name IS 'service provider name';

COMMENT ON TABLE intervention IS 'intervention details';
COMMENT ON COLUMN intervention.id IS 'intervention unique identifier';
COMMENT ON COLUMN intervention.dynamic_framework_contract_id IS 'dynamic framework unique identifier';
COMMENT ON COLUMN intervention.created_at IS 'when the record was added';
COMMENT ON COLUMN intervention.title IS 'intervention name';
COMMENT ON COLUMN intervention.description IS 'intervention description';
COMMENT ON COLUMN intervention.incoming_referral_distribution_email IS 'email address receiving notifications about new referrals';

COMMENT ON TABLE metadata IS 'defines metadata about the schema';
COMMENT ON COLUMN metadata.table_name IS 'which table this record is describing';
COMMENT ON COLUMN metadata.column_name IS 'which column this record is describing';
COMMENT ON COLUMN metadata.sensitive IS '`true` means the contents should be obfuscated/anonymised in unsafe environments; `false` means it’s safe to see/copy';
COMMENT ON COLUMN metadata.domain_data IS '`true` means the field hold operational/domain data; `false` means it is an internal structure that holds no operational data';

COMMENT ON TABLE contract_type IS 'contract (service) types available for commissioned rehabilitative services';
COMMENT ON COLUMN contract_type.id IS 'service-owned unique identifier';
COMMENT ON COLUMN contract_type.name IS 'name of the contract';
COMMENT ON COLUMN contract_type.code IS 'reference code for this contract type';

COMMENT ON TABLE contract_type_service_category IS 'configuration of selectable service categories for each contract type';
COMMENT ON COLUMN contract_type_service_category.contract_type_id IS 'reference to the contact type id for current record';
COMMENT ON COLUMN contract_type_service_category.service_category_id IS 'reference to the service category id for current record';

COMMENT ON TABLE dynamic_framework_contract_sub_contractor IS 'sub-contractors for each commissioned rehabilitative services contract';
COMMENT ON COLUMN dynamic_framework_contract_sub_contractor.dynamic_framework_contract_id IS 'reference to the unique identifier for the contract record';
COMMENT ON COLUMN dynamic_framework_contract_sub_contractor.subcontractor_provider_id IS 'unique value for subcontractor provider';

COMMENT ON TABLE criminogenic_need IS 'mapping for the criminogenic needs that this intervention meets';
COMMENT ON COLUMN criminogenic_need.need_id IS 'reference containing the need identifier from the appropriate reference table';
COMMENT ON COLUMN criminogenic_need.intervention_id IS 'the identifier of the applicable intervention record';

COMMENT ON TABLE criminogenic_need_ref IS '**reference data** list of available criminogenic need descriptions';
COMMENT ON COLUMN criminogenic_need_ref.id IS 'service-owned unique identifier';
COMMENT ON COLUMN criminogenic_need_ref.name IS 'name given to the criminogenic need';

COMMENT ON TABLE delivery_location IS 'details of the location where applicable intervention can be delivered';
COMMENT ON COLUMN delivery_location.intervention_id IS 'reference of the intervention record that is applicable';
COMMENT ON COLUMN delivery_location.provider_name IS 'name of the provider of this delivery location';
COMMENT ON COLUMN delivery_location.contact IS 'contact details for the provider';
COMMENT ON COLUMN delivery_location.pdu_establishments IS 'description of the probation delivery units that this location covers';

COMMENT ON TABLE enabling_intervention IS 'description of any enabling interventions that this one needs prior to delivery';
COMMENT ON COLUMN enabling_intervention.id IS 'service-owned unique identifier';
COMMENT ON COLUMN enabling_intervention.intervention_id IS 'reference to the intervention which this enabling detail applies';
COMMENT ON COLUMN enabling_intervention.enabling_intervention_detail IS 'description of the details of any enabling intervention(s)';

COMMENT ON TABLE ethnicity_ref IS '**reference data** list of available ethnicity descriptions';
COMMENT ON COLUMN ethnicity_ref.id IS 'service-owned unique identifier';
COMMENT ON COLUMN ethnicity_ref.broad_group IS 'broad ethnic group description';
COMMENT ON COLUMN ethnicity_ref.name IS 'specific name for this ethnic group';

COMMENT ON TABLE excluded_offence IS 'details of offences which would prevent the user from attending this intervention';
COMMENT ON COLUMN excluded_offence.id IS 'service-owned unique identifier';
COMMENT ON COLUMN excluded_offence.offence_type_id IS 'identifier determining the type of offence that this intervention cannot be participated with';
COMMENT ON COLUMN excluded_offence.intervention_id IS 'the identifier for the excluded intervention';
COMMENT ON COLUMN excluded_offence.victim_type IS 'description of the type of victim for the associated excluded offence';
COMMENT ON COLUMN excluded_offence.motivation IS 'description of motivation of the offender for excluded offence';

COMMENT ON TABLE exclusion IS 'detailed information on conditions that would exclude from a particular intervention';
COMMENT ON COLUMN exclusion.id IS 'service-owned unique identifier';
COMMENT ON COLUMN exclusion.intervention_id IS 'the identifier for the excluded intervention';
COMMENT ON COLUMN exclusion.min_remaining_sentence_duration_guide IS 'guidance text relating to remaining sentence';
COMMENT ON COLUMN exclusion.remaining_license_community_order_guide IS 'guidance text relating to remaining community order of sebtence';
COMMENT ON COLUMN exclusion.alcohol_drug_problem_guide IS 'guidance text relating to alcohol or drug related problems';
COMMENT ON COLUMN exclusion.mental_health_problem_guide IS 'guidance text relating to mental health conditions';
COMMENT ON COLUMN exclusion.other_preferred_method_guide IS 'guidance text relating to other preferred methods of rehabilitation';
COMMENT ON COLUMN exclusion.same_type_rule_guide IS 'guidance text relating to same type rules';
COMMENT ON COLUMN exclusion.schedule_frequency_guide IS 'guidance text relating to frequence of schedule';

COMMENT ON TABLE intervention_catalogue IS 'global list of interventions which are available';
COMMENT ON COLUMN intervention_catalogue.id IS 'service-owned unique identifier';
COMMENT ON COLUMN intervention_catalogue.name IS 'name of the intervention';
COMMENT ON COLUMN intervention_catalogue.int_type IS 'type of the intevention';
COMMENT ON COLUMN intervention_catalogue.short_description IS 'a shortended text description of the intervention';
COMMENT ON COLUMN intervention_catalogue.long_description IS 'a longer full description of the intervention';
COMMENT ON COLUMN intervention_catalogue.topic IS 'the applicable topic for this intervention';
COMMENT ON COLUMN intervention_catalogue.session_detail IS 'detail description of number / content if delivery sessions for the intervention';
COMMENT ON COLUMN intervention_catalogue.commencement_date IS 'the date when intervention can accept referrals';
COMMENT ON COLUMN intervention_catalogue.termination_date IS 'the end date when intervention on longer accepts referrals';
COMMENT ON COLUMN intervention_catalogue.created IS 'date the intervention record was created';
COMMENT ON COLUMN intervention_catalogue.created_by IS 'auth identifier of the user that created this intervention record';
COMMENT ON COLUMN intervention_catalogue.last_modified IS 'date the intervention record was last modified';
COMMENT ON COLUMN intervention_catalogue.last_modified_by IS 'auth identifier of the user that last mofified this intervention record';
COMMENT ON COLUMN intervention_catalogue.reason_for_referral IS 'valid reason that referral to this intervention can be made';

COMMENT ON TABLE intervention_catalogue_map IS 'mapping table covering where there are more than one matching implementation';
COMMENT ON COLUMN intervention_catalogue_map.intervention_catalogue_id IS 'service-owned unique identifier';
COMMENT ON COLUMN intervention_catalogue_map.intervention_id IS 'identifier of the intervention to which this mapping record applies';

COMMENT ON TABLE offence_type_ref IS '**reference data** list of offence types';
COMMENT ON COLUMN offence_type_ref.id IS 'service-owned unique identifier';
COMMENT ON COLUMN offence_type_ref.name IS 'specific name for this offence type';

COMMENT ON TABLE personal_eligibility IS 'personal details of profile of eliglible persons';
COMMENT ON COLUMN personal_eligibility.id IS 'service-owned unique identifier';
COMMENT ON COLUMN personal_eligibility.intervention_id IS 'identifier of the intervention to which this personal eligibility is specified';
COMMENT ON COLUMN personal_eligibility.min_age IS 'minimum allowable age for this intervention';
COMMENT ON COLUMN personal_eligibility.max_age IS 'maximum allowable age for this intervention';
COMMENT ON COLUMN personal_eligibility.males IS 'does the intervention allow referral of male service users';
COMMENT ON COLUMN personal_eligibility.females IS 'does the intervention allow referral of female service users';

COMMENT ON TABLE possible_outcome IS 'list of outcomes that can occur for a given intervention';
COMMENT ON COLUMN possible_outcome.id IS 'service-owned unique identifier';
COMMENT ON COLUMN possible_outcome.intervention_id IS 'identifier of the intervention to which possible outcome data applies';
COMMENT ON COLUMN possible_outcome.outcome IS 'applicable possible outcome description guidance text';

COMMENT ON TABLE risk_consideration IS 'factors that determine eligibility based on risk';
COMMENT ON COLUMN risk_consideration.id IS 'service-owned unique identifier';
COMMENT ON COLUMN risk_consideration.intervention_id IS 'identifier of the intervention to which risk consideration data applies';
COMMENT ON COLUMN risk_consideration.cn_score_guide IS 'guidance text on cn score';
COMMENT ON COLUMN risk_consideration.extremism_risk_guide IS 'guidance text on extremism risk';
COMMENT ON COLUMN risk_consideration.sara_partner_score_guide IS 'guidance text on sara partner score';
COMMENT ON COLUMN risk_consideration.sara_other_score_guide IS 'guidance text on sara risk for other individuals score';
COMMENT ON COLUMN risk_consideration.osp_score_guide IS 'guidance text on ';
COMMENT ON COLUMN risk_consideration.osp_dc_icc_combination_guide IS 'guidance text on ';
COMMENT ON COLUMN risk_consideration.ogrs_score_guide IS 'guidance text on ogrs score';
COMMENT ON COLUMN risk_consideration.ovp_guide IS 'guidance text on ovp';
COMMENT ON COLUMN risk_consideration.ogp_guide IS 'guidance text on ogp';
COMMENT ON COLUMN risk_consideration.pna_guide IS 'guidance text on pna';
COMMENT ON COLUMN risk_consideration.rosh_level IS 'guidance text on risk of serious harm level';
COMMENT ON COLUMN risk_consideration.rsr_guide IS 'guidance text on rsr';

COMMENT ON TABLE special_educational_need IS 'special educational needs that this particular intervention caters for';
COMMENT ON COLUMN special_educational_need.id IS 'service-owned unique identifier';
COMMENT ON COLUMN special_educational_need.intervention_id IS 'identifier of the intervention to which special educational needs data applies';
COMMENT ON COLUMN special_educational_need.literacy_level_guide IS 'guidance text on literacy level';
COMMENT ON COLUMN special_educational_need.learning_disability_catered_for IS 'guidance text on learning disability provisions';
COMMENT ON COLUMN special_educational_need.equivalent_non_ldc_programme_guide IS 'guidance text on equivalent non ldc programme';