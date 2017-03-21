DROP TABLE IF EXISTS BALANCES;
DROP TABLE IF EXISTS OPERATIONS;
DROP TABLE IF EXISTS ACCOUNTS;
DROP TABLE IF EXISTS CATEGORIES;
DROP TABLE IF EXISTS CURRENCIES;

CREATE TABLE CURRENCIES (
  ID    INTEGER      NOT NULL AUTO_INCREMENT,
  CODE  VARCHAR(3)   NOT NULL,
  TITLE VARCHAR(250) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE CATEGORIES (
  ID    INTEGER      NOT NULL AUTO_INCREMENT,
  TITLE VARCHAR(250) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE ACCOUNTS (
  ID          INTEGER      NOT NULL AUTO_INCREMENT,
  CURRENCY_ID INTEGER      NOT NULL,
  TITLE       VARCHAR(250) NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_ACCOUNTS_CURRENCIES FOREIGN KEY (CURRENCY_ID) REFERENCES CURRENCIES (ID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE TABLE OPERATIONS (
  ID          INTEGER        NOT NULL  AUTO_INCREMENT,
  ACCOUNT_ID  INTEGER        NOT NULL,
  CATEGORY_ID INTEGER        NOT NULL,
  DAY         DATE           NOT NULL,
  AMOUNT      NUMERIC(20, 4) NOT NULL,
  NOTE        VARCHAR(250),
  PRIMARY KEY (ID),
  CONSTRAINT FK_OPERATIONS_ACCOUNTS FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNTS (ID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT FK_OPERATIONS_CATEGORIES FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORIES (ID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE TABLE BALANCES (
  ACCOUNT_ID INTEGER        NOT NULL,
  DAY        DATE           NOT NULL,
  AMOUNT     NUMERIC(20, 4) NOT NULL,
  MANUAL     BOOLEAN        NOT NULL,
  PRIMARY KEY (ACCOUNT_ID, DAY),
  CONSTRAINT FK_BALANCES_ACCOUNTS FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNTS (ID)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);
