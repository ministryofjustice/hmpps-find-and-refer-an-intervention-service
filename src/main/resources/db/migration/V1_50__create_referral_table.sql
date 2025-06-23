CREATE TABLE referral
(
    id               UUID PRIMARY KEY,
    event_type       TEXT NOT NULL,
    main_type        TEXT NOT NULL,
    person_reference TEXT NOT NULL
);

COMMENT ON COLUMN referral.id IS 'The Unique ID for the referral.';
COMMENT ON COLUMN referral.event_type IS 'The type of the event that has been processed.';
COMMENT ON COLUMN referral.main_type IS 'Contains the type of intervention the referral is for';
COMMENT ON COLUMN referral.person_reference IS 'The CRN or Prison Number of the person being referred';

INSERT INTO metadata(table_name, column_name, sensitive, domain_data)
VALUES ('referral', 'id', false, false),
       ('referral', 'event_type', false, false),
       ('referral', 'main_type', false, false),
       ('referral', 'person_reference', true, false);