CREATE TABLE rates (
  id               SERIAL          NOT NULL,
  currency_id_from INTEGER         NOT NULL,
  currency_id_to   INTEGER         NOT NULL,
  day              DATE,
  value            NUMERIC(20, 10) NOT NULL,
  CONSTRAINT pk_rates PRIMARY KEY (id),
  CONSTRAINT fk_rates_currencies_currency_id_from FOREIGN KEY (currency_id_from) REFERENCES currencies (id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,
  CONSTRAINT fk_rates_currencies_currency_id_to FOREIGN KEY (currency_id_to) REFERENCES currencies (id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT
);
