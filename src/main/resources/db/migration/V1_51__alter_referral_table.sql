CREATE TYPE person_reference_type AS ENUM ('CRN','NOMS');
CREATE TYPE sourced_from_reference_type AS ENUM ('LICENCE_CONDITION', 'REQUIREMENT');

ALTER TABLE referral
    DROP COLUMN event_type,
    DROP COLUMN main_type,
    ADD COLUMN setting                     setting_type                NOT NULL,
    ADD COLUMN intervention_type           intervention_type           NOT NULL,
    ADD COLUMN intervention_name           TEXT                        NOT NULL,
    ADD COLUMN person_reference_type       person_reference_type       NOT NULL,
    ADD COLUMN sourced_from_reference_type sourced_from_reference_type NOT NULL,
    ADD COLUMN sourced_from_reference      TEXT                        NOT NULL;

COMMENT ON TABLE referral IS 'Stores basic information about a referral to an Intervention';
COMMENT ON COLUMN referral.setting IS 'The setting that the referral is for.';
COMMENT ON COLUMN referral.intervention_type IS 'The type of the intervention that the person is being referred to.';
COMMENT ON COLUMN referral.intervention_name IS 'The name of the intervention that the person is being referred to.';
COMMENT ON COLUMN referral.person_reference_type IS 'The type of identifier that the person_reference column is populated with either CRN or NOMS.';
COMMENT ON COLUMN referral.sourced_from_reference_type IS 'The type of the event that the referral was sourced from.';
COMMENT ON COLUMN referral.sourced_from_reference IS 'The identifier for the source of the event.';

INSERT INTO metadata(table_name, column_name, sensitive, domain_data)
VALUES ('referral', 'setting', false, true),
       ('referral', 'intervention_type', false, true),
       ('referral', 'intervention_name', false, true),
       ('referral', 'person_reference_type', false, false),
       ('referral', 'sourced_from_reference_type', false, false),
       ('referral', 'sourced_from_reference', false, false);
