DROP TABLE IF EXISTS balances;
DROP TABLE IF EXISTS operations;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS currencies;

CREATE TABLE currencies (
  id    SERIAL       NOT NULL,
  code  VARCHAR(3)   NOT NULL,
  title VARCHAR(250) NOT NULL,
  CONSTRAINT pk_currencies PRIMARY KEY (id)
);

CREATE TABLE categories (
  id    SERIAL       NOT NULL,
  title VARCHAR(250) NOT NULL,
  CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE accounts (
  id          SERIAL       NOT NULL,
  currency_id INTEGER      NOT NULL,
  title       VARCHAR(250) NOT NULL,
  CONSTRAINT pk_accounts PRIMARY KEY (id),
  CONSTRAINT fk_accounts_currencies FOREIGN KEY (currency_id) REFERENCES currencies (id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT
);

CREATE TABLE operations (
  id          SERIAL         NOT NULL,
  account_id  INTEGER        NOT NULL,
  category_id INTEGER        NOT NULL,
  day         DATE           NOT NULL,
  amount      NUMERIC(20, 4) NOT NULL,
  note        VARCHAR(250),
  CONSTRAINT pk_operations PRIMARY KEY (id),
  CONSTRAINT fk_operations_accounts FOREIGN KEY (account_id) REFERENCES accounts (id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,
  CONSTRAINT fk_operations_categories FOREIGN KEY (category_id) REFERENCES categories (id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT
);

CREATE TABLE balances (
  account_id INTEGER        NOT NULL,
  day        DATE           NOT NULL,
  amount     NUMERIC(20, 4) NOT NULL,
  manual     BOOLEAN        NOT NULL,
  CONSTRAINT pk_balances PRIMARY KEY (account_id, day),
  CONSTRAINT fk_balances_accounts FOREIGN KEY (account_id) REFERENCES accounts (id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT
);
