ALTER TABLE CURRENCIES ADD COLUMN ACCURACY INTEGER;
UPDATE CURRENCIES SET ACCURACY = 4;
ALTER TABLE CURRENCIES MODIFY ACCURACY INTEGER NOT NULL;
