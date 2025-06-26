CREATE TABLE message_history
(
    id    UUID PRIMARY KEY,
    event jsonb NOT NULL
);

COMMENT ON TABLE message_history IS 'Contains the raw event json to capture history.';

COMMENT ON COLUMN message_history.id IS 'Unique ID of the event in our db.';
COMMENT ON COLUMN message_history.event IS 'Raw json of the received event.';

INSERT INTO metadata(table_name, column_name, sensitive, domain_data)
VALUES ('message_history', 'id', false, false)
     , ('message_history', 'event', false, false);