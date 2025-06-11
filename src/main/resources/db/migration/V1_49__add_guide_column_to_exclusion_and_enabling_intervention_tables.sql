ALTER TABLE enabling_intervention
    ADD COLUMN convicted_for_offence_type_guide TEXT NULL;
COMMENT ON COLUMN enabling_intervention.convicted_for_offence_type_guide IS 'Formatted text which outlines any criteria which would are required for an individual to undertake this intervention';

ALTER TABLE exclusion
    ADD COLUMN convicted_for_offence_type_guide TEXT NULL;
COMMENT ON COLUMN exclusion.convicted_for_offence_type_guide IS 'Formatted text which outlines any criteria which would exclude an individual from this intervention';

INSERT INTO metadata(table_name, column_name, sensitive, domain_data)
VALUES ('enabling_intervention', 'convicted_for_offence_type_guide', false, true),
       ('exclusion', 'convicted_for_offence_type_guide', false, true);
