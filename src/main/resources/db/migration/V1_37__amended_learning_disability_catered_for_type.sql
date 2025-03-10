ALTER TABLE special_educational_need
    ALTER COLUMN learning_disability_catered_for TYPE TEXT,
    ALTER COLUMN learning_disability_catered_for DROP NOT NULL,
    ALTER COLUMN learning_disability_catered_for DROP DEFAULT;
