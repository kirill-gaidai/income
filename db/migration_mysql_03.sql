CREATE TABLE RATES (
  ID               INTEGER         NOT NULL AUTO_INCREMENT,
  CURRENCY_ID_FROM INTEGER         NOT NULL,
  CURRENCY_ID_TO   INTEGER         NOT NULL,
  DAY              DATE,
  VALUE            NUMERIC(20, 10) NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_RATES_CURRENCIES_CURRENCY_ID_FROM FOREIGN KEY (CURRENCY_ID_FROM) REFERENCES CURRENCIES (ID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT FK_RATES_CURRENCIES_CURRENCY_ID_TO FOREIGN KEY (CURRENCY_ID_TO) REFERENCES CURRENCIES (ID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);
