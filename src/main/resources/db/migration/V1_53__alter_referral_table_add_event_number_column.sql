ALTER TABLE referral
    ADD COLUMN event_number Integer NOT NULL DEFAULT 1;

COMMENT ON COLUMN referral.event_number IS 'The number of the event that has been sent for this referral.';

INSERT INTO metadata(table_name, column_name, sensitive, domain_data)
VALUES ('referral', 'event_number', false, false);