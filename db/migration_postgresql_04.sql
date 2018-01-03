CREATE TABLE users (
  id       SERIAL        NOT NULL,
  login    VARCHAR(10)   NOT NULL,
  password VARCHAR(1024) NOT NULL,
  admin    BOOLEAN       NOT NULL,
  blocked  BOOLEAN       NOT NULL,
  token    VARCHAR(20)   NOT NULL,
  expires  TIMESTAMP     NOT NULL,
  CONSTRAINT pk_users PRIMARY KEY (id)
);
