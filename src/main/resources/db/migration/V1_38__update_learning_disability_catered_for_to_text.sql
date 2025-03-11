ALTER TABLE postgres.public.special_educational_need
    ALTER COLUMN learning_disability_catered_for DROP NOT NULL,
    ALTER COLUMN learning_disability_catered_for TYPE TEXT USING
        CASE
            WHEN learning_disability_catered_for = true THEN 'Yes'
            WHEN learning_disability_catered_for = false THEN 'No'
            END;