ALTER TABLE intervention_catalogue
    ADD COLUMN time_to_complete text NULL;

INSERT INTO metadata (table_name, column_name, sensitive, domain_data)
VALUES ('intervention_catalogue', 'time_to_complete', 'FALSE', 'TRUE')