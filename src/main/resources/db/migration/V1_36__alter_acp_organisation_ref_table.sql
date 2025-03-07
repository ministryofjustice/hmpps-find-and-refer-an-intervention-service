ALTER TABLE organisation
ADD COLUMN category text,
ADD COLUMN county text ;

COMMENT ON COLUMN organisation.category IS 'The prison category e.g A, B, C etc';
COMMENT ON COLUMN organisation.county IS 'The county where prison is located';

INSERT INTO metadata (table_name, column_name, sensitive, domain_data)
VALUES ('organisation', 'category', false, true),
       ('organisation', 'county', false, true);