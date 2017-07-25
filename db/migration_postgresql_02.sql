ALTER TABLE currencies ADD COLUMN precision SMALLINT;
UPDATE currencies SET precision = 4;
ALTER TABLE currencies ALTER COLUMN precision SET NOT NULL;
