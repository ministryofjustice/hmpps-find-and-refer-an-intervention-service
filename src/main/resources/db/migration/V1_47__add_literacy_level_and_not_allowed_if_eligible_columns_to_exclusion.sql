ALTER TABLE exclusion
    ADD COLUMN not_allowed_if_eligible_for_another_intervention_guide text NULL,
    ADD COLUMN literacy_level_guide                                   text NULL;

COMMENT ON COLUMN exclusion.not_allowed_if_eligible_for_another_intervention_guide IS 'Formatted text for advising which other interventions exclude an individual from the current intervention';
COMMENT ON COLUMN exclusion.literacy_level_guide IS 'Formatted text advising the level of literacy required for this intervention';

INSERT INTO metadata (table_name, column_name, sensitive, domain_data)
VALUES ('exclusion', 'not_allowed_if_eligible_for_another_intervention_guide', false, false),
       ('exclusion', 'literacy_level_guide', false, false);