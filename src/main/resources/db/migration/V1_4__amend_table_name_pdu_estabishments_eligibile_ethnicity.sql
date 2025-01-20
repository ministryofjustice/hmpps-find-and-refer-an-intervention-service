ALTER TABLE delivery_location
    RENAME COLUMN pdu_estabishments to pdu_establishments;

ALTER TABLE eligibile_ethnicity
    RENAME to eligible_ethnicity;

ALTER TABLE eligibile_offence
    RENAME to eligible_offence;

ALTER TABLE intervention_catalogue
    RENAME COLUMN short_decription to short_description;
ALTER TABLE intervention_catalogue
    RENAME COLUMN long_decription to long_description;