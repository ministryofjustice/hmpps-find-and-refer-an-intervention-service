CREATE TABLE message
(
    id          UUID PRIMARY KEY UNIQUE       NOT NULL,
    referral_id UUID REFERENCES referral (id) NULL,
    event       JSONB                         NOT NULL
);

COMMENT ON TABLE message IS 'Contains the raw event json to capture history.';

COMMENT ON COLUMN message.id IS 'Unique ID of the event in our db.';
COMMENT ON COLUMN message.event IS 'Raw json of the received event.';

INSERT INTO metadata(table_name, column_name, sensitive, domain_data)
VALUES ('message', 'id', false, false),
       ('message', 'referral_id', false, false),
       ('message', 'event', false, false);