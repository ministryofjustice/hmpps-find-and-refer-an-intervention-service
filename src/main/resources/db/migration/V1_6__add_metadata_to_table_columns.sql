COMMENT ON TABLE service_category IS '**reference data** intervention service categories, which relate to service user needs';
COMMENT ON COLUMN service_category.id IS 'service-owned unique identifier';
COMMENT ON COLUMN service_category.created IS 'when the record was added';
COMMENT ON COLUMN service_category.name IS 'intervention service category';

ALTER TABLE pcc_region RENAME COLUMN nps_region TO nps_region_id;
COMMENT ON TABLE pcc_region IS '**reference data** Police and Crime Commissioner (PCC) region details';
COMMENT ON COLUMN pcc_region.id IS 'PCC region unique identifier';
COMMENT ON COLUMN pcc_region.name IS 'PCC region name';
COMMENT ON COLUMN pcc_region.nps_region_id IS 'the ID of the National Probation Service (NPS) region the PCC is in';

COMMENT ON TABLE contract_type IS 'contract (service) types available for commissioned rehabilitative services';

COMMENT ON TABLE contract_type_service_category IS 'configuration of selectable service categories for each contract type';

COMMENT ON TABLE complexity_level IS 'details about the complexity levels of the service category';
COMMENT ON COLUMN complexity_level.id IS 'service-owned unique identifier';
COMMENT ON COLUMN complexity_level.title IS 'complexity level of needs, usually: low, medium, high';
COMMENT ON COLUMN complexity_level.description IS 'rationale for the complexity level';
COMMENT ON COLUMN complexity_level.service_category_id IS 'the ID of the intervention service category it belongs to';

ALTER TABLE nps_region ALTER COLUMN name SET NOT NULL;
ALTER TABLE ONLY nps_region ADD CONSTRAINT nps_region_name_key UNIQUE (name);
COMMENT ON TABLE nps_region IS '**reference data** National Probation Service (NPS) region details';
COMMENT ON COLUMN nps_region.id IS 'the ID of the NPS region unique identifier';
COMMENT ON COLUMN nps_region.name IS 'NPS region name';

COMMENT ON TABLE desired_outcome IS '**reference data** desired outcomes available for each intervention service category';
COMMENT ON COLUMN desired_outcome.id IS 'service-owned unique identifier';
COMMENT ON COLUMN desired_outcome.description IS 'describes what the outcome should be for the service user';
COMMENT ON COLUMN desired_outcome.service_category_id IS 'the ID of the intervention service category it belongs to';

COMMENT ON TABLE dynamic_framework_contract IS 'stores the scope of the commissioned rehabilitative services contracts';
COMMENT ON COLUMN dynamic_framework_contract.id IS 'service-owned unique identifier';
COMMENT ON COLUMN dynamic_framework_contract.prime_provider_id IS 'awarded for a (prime) provider';
COMMENT ON COLUMN dynamic_framework_contract.start_date IS 'when delivery can start';
COMMENT ON COLUMN dynamic_framework_contract.end_date IS 'when delivery must end';
COMMENT ON COLUMN dynamic_framework_contract.nps_region_id IS 'awarded to deliver in either an NPS or PCC region';
COMMENT ON COLUMN dynamic_framework_contract.minimum_age IS 'the minimum age of attending service users';
COMMENT ON COLUMN dynamic_framework_contract.pcc_region_id IS 'awarded to deliver in either an NPS or PCC region';
COMMENT ON COLUMN dynamic_framework_contract.allows_female IS 'whether female service users can attend';
COMMENT ON COLUMN dynamic_framework_contract.maximum_age IS 'the maximum age of attending service users';
COMMENT ON COLUMN dynamic_framework_contract.allows_male IS 'whether can male service users can attend';

COMMENT ON TABLE dynamic_framework_contract_sub_contractor IS 'sub-contractors for each commissioned rehabilitative services contract';

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

ALTER TABLE intervention_catalogue ALTER COLUMN short_description drop not null;
ALTER TABLE intervention_catalogue RENAME COLUMN reasons_for_referral to reason_for_referral;
