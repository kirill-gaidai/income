ALTER TABLE currencies ADD COLUMN accuracy INTEGER;
UPDATE currencies SET accuracy = 4;
ALTER TABLE currencies ALTER COLUMN accuracy SET NOT NULL;
