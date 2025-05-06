ALTER TABLE delivery_method_setting
    RENAME COLUMN delivery_method_id TO intervention_id;

ALTER TABLE delivery_method_setting
    DROP CONSTRAINT fk__dms__delivery_method_id;

ALTER TABLE delivery_method_setting
    ADD CONSTRAINT fk__dms__intervention_id FOREIGN KEY (intervention_id) REFERENCES intervention_catalogue;

COMMENT ON COLUMN delivery_method_setting.intervention_id IS 'the ID of the intervention delivery method setting belongs to';
