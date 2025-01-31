ALTER TABLE delivery_method
    ADD COLUMN delivery_format text NULL;

ALTER TABLE delivery_method
    ADD COLUMN attendance_type text NULL;

ALTER TABLE dynamic_framework_contract
    ALTER COLUMN minimum_age drop NOT NULL;

ALTER TABLE dynamic_framework_contract
    ALTER COLUMN minimum_age drop DEFAULT;

ALTER TABLE dynamic_framework_contract
    ALTER COLUMN maximum_age drop NOT NULL;

ALTER TABLE dynamic_framework_contract
    ALTER COLUMN maximum_age drop DEFAULT;

ALTER TABLE personal_eligibility
    ALTER COLUMN min_age DROP DEFAULT;

ALTER TABLE personal_eligibility
    ALTER COLUMN max_age DROP DEFAULT;