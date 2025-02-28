ALTER TABLE personal_eligibility
    ADD COLUMN who_is_eligible_guide text NULL;

COMMENT ON COLUMN personal_eligibility.who_is_eligible_guide IS 'This column holds text to be represented on the interventions details page under "Who is eligible" section.';

INSERT INTO metadata(table_name, column_name, sensitive, domain_data)
VALUES ('personal_eligibility', 'who_is_eligible_guide', false, true);