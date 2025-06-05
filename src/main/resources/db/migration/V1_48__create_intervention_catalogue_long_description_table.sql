CREATE TABLE intervention_catalogue_long_description (
    intervention_id UUID NOT NULL,
    long_description TEXT
);

INSERT INTO metadata (table_name, column_name, sensitive, domain_data) VALUES
('intervention_catalogue_long_description','intervention_id',FALSE, TRUE),
('intervention_catalogue_long_description','long_description',FALSE, TRUE)