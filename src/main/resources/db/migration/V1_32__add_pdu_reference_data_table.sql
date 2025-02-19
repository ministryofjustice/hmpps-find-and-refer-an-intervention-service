CREATE TABLE pdu_ref
(
    id            text NOT NULL,
    name          text NOT NULL,
    pcc_region_id text NOT NULL,
    CONSTRAINT pdu_pkey PRIMARY KEY (id),
    CONSTRAINT pdu_name_key UNIQUE (name),
    CONSTRAINT fk__pdu__pcc_region_id FOREIGN KEY (pcc_region_id) REFERENCES pcc_region (id)

);

COMMENT ON TABLE pdu_ref IS '**reference data** Probation Delivery Unit (PDU) details';

COMMENT ON COLUMN pdu_ref.id IS 'PDU unique identifier';

COMMENT ON COLUMN pdu_ref.name IS 'PDU name';