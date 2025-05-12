ALTER TABLE delivery_method_setting
    DROP COLUMN IF EXISTS delivery_method_id;

ALTER TABLE delivery_method_setting
    ADD COLUMN intervention_id uuid;

ALTER TABLE delivery_method_setting
    ALTER COLUMN intervention_id SET NOT NULL;

ALTER TABLE delivery_method_setting
    DROP CONSTRAINT IF EXISTS fk__dms__delivery_method_id;

ALTER TABLE delivery_method_setting
    ADD CONSTRAINT fk__dms__intervention_id FOREIGN KEY (intervention_id) REFERENCES intervention_catalogue;

COMMENT ON COLUMN delivery_method_setting.intervention_id IS 'the ID of the intervention delivery method setting belongs to';

UPDATE metadata
   SET column_name = 'intervention_id'
 WHERE table_name = 'delivery_method_setting'
   AND column_name = 'delivery_method_id';