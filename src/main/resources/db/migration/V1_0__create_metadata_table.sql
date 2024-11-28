CREATE TABLE metadata
(
    table_name  text NOT NULL,
    column_name text NOT NULL,
    sensitive   boolean NOT NULL,
    domain_data boolean NOT NULL
);
