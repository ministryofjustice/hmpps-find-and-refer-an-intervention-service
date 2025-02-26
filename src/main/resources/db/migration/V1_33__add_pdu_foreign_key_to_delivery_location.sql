DELETE
FROM delivery_location;

ALTER TABLE delivery_location
    DROP COLUMN pdu_establishments,
    ALTER COLUMN provider_name DROP NOT NULL,
    ALTER COLUMN contact DROP NOT NULL;

ALTER TABLE delivery_location
    ADD COLUMN pdu_ref_id text NULL,
    ADD CONSTRAINT fk__delivery_location_pdu_id FOREIGN KEY (pdu_ref_id) REFERENCES pdu_ref (id);