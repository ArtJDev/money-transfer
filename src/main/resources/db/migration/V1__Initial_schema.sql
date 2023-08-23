CREATE TABLE accounts
(
    id             BIGSERIAL NOT NULL PRIMARY KEY,
    account_number INTEGER   NOT NULL,
    account_owner  VARCHAR   NOT NULL,
    pin_number     INTEGER   NOT NULL,
    balance        DOUBLE PRECISION,
    created_date   TIMESTAMP
);
CREATE TABLE history
(
    id              BIGSERIAL NOT NULL PRIMARY KEY,
    date            VARCHAR,
    date_time       TIMESTAMP,
    event           VARCHAR,
    account_number  INTEGER,
    owner_name      VARCHAR,
    account_owner   VARCHAR,
    amount_transfer VARCHAR,
    amount          DOUBLE PRECISION
);
INSERT INTO accounts (account_number, account_owner, pin_number, balance, created_date)
VALUES (111111, 'ARTEM', 111, 5000.0, NOW());
INSERT INTO accounts (account_number, account_owner, pin_number, balance, created_date)
VALUES (222222, 'OXANA', 222, 3000.0, NOW());
