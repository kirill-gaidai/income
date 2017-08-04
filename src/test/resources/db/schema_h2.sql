CREATE TABLE CURRENCIES (
  ID       INTEGER      NOT NULL AUTO_INCREMENT,
  CODE     VARCHAR(3)   NOT NULL,
  TITLE    VARCHAR(250) NOT NULL,
  ACCURACY INTEGER      NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE CATEGORIES (
  ID    INTEGER      NOT NULL AUTO_INCREMENT,
  SORT  VARCHAR(10)  NOT NULL,
  TITLE VARCHAR(250) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE ACCOUNTS (
  ID          INTEGER      NOT NULL AUTO_INCREMENT,
  CURRENCY_ID INTEGER      NOT NULL,
  SORT        VARCHAR(10)  NOT NULL,
  TITLE       VARCHAR(250) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE OPERATIONS (
  ID          INTEGER        NOT NULL  AUTO_INCREMENT,
  ACCOUNT_ID  INTEGER        NOT NULL,
  CATEGORY_ID INTEGER        NOT NULL,
  DAY         DATE           NOT NULL,
  AMOUNT      NUMERIC(20, 4) NOT NULL,
  NOTE        VARCHAR(250),
  PRIMARY KEY (ID)
);

CREATE TABLE BALANCES (
  ACCOUNT_ID INTEGER        NOT NULL,
  DAY        DATE           NOT NULL,
  AMOUNT     NUMERIC(20, 4) NOT NULL,
  MANUAL     BOOLEAN        NOT NULL,
  PRIMARY KEY (ACCOUNT_ID, DAY)
);

CREATE TABLE RATES (
  ID               INTEGER         NOT NULL AUTO_INCREMENT,
  CURRENCY_ID_FROM INTEGER         NOT NULL,
  CURRENCY_ID_TO   INTEGER         NOT NULL,
  DAY              DATE,
  VALUE            NUMERIC(20, 10) NOT NULL,
  PRIMARY KEY (ID)
);
