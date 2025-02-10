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

COMMENT ON TABLE desired_outcome IS '**reference data** desired outcomes available for each intervention service category';
COMMENT ON COLUMN desired_outcome.id IS 'service-owned unique identifier';
COMMENT ON COLUMN desired_outcome.service_category_id IS 'the ID of the intervention service category it belongs to';
COMMENT ON COLUMN desired_outcome.description IS 'describes what the outcome should be for the service user';

COMMENT ON TABLE nps_region IS '**reference data** National Probation Service (NPS) region details';
COMMENT ON COLUMN nps_region.id IS 'the ID of the NPS region unique identifier';
COMMENT ON COLUMN nps_region.name IS 'NPS region name';

COMMENT ON TABLE pcc_region IS '**reference data** Police and Crime Commissioner (PCC) region details';
COMMENT ON COLUMN pcc_region.id IS 'PCC region unique identifier';
COMMENT ON COLUMN pcc_region.name IS 'PCC region name';
COMMENT ON COLUMN pcc_region.nps_region_id IS 'the ID of the National Probation Service (NPS) region the PCC is in';

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