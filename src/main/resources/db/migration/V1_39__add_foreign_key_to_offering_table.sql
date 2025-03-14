ALTER TABLE offering
    ADD CONSTRAINT offering_organisation_fk FOREIGN KEY (organisation_id) REFERENCES organisation (code);