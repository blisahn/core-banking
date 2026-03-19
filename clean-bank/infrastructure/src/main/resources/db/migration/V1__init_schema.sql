-- Customers
CREATE TABLE customers (
    id            UUID         NOT NULL PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    date_of_birth DATE         NOT NULL,
    street        VARCHAR(255) NOT NULL,
    district      VARCHAR(255) NOT NULL,
    status        VARCHAR(50)  NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL
);

-- Accounts
CREATE TABLE accounts (
    id               UUID           NOT NULL PRIMARY KEY,
    account_number   VARCHAR(50)    NOT NULL UNIQUE,
    customer_id      UUID           NOT NULL REFERENCES customers(id),
    balance_amount   NUMERIC(19, 2) NOT NULL,
    balance_currency VARCHAR(3)     NOT NULL,
    type             VARCHAR(50)    NOT NULL,
    status           VARCHAR(50)    NOT NULL,
    created_at       TIMESTAMPTZ    NOT NULL,
    updated_at       TIMESTAMPTZ    NOT NULL
);

-- Transactions
CREATE TABLE transactions (
    id                UUID           NOT NULL PRIMARY KEY,
    source_account_id UUID           REFERENCES accounts(id),
    target_account_id UUID           REFERENCES accounts(id),
    amount            NUMERIC(19, 2) NOT NULL,
    currency          VARCHAR(3)     NOT NULL,
    type              VARCHAR(50)    NOT NULL,
    status            VARCHAR(50)    NOT NULL,
    description       VARCHAR(500)   NOT NULL,
    timestamp         TIMESTAMPTZ    NOT NULL,
    created_at        TIMESTAMPTZ    NOT NULL,
    updated_at        TIMESTAMPTZ    NOT NULL
);

-- Outbox Events
CREATE TABLE outbox_events (
    id             UUID        NOT NULL PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id   UUID        NOT NULL,
    event_type     VARCHAR(255) NOT NULL,
    payload        TEXT        NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL,
    processed      BOOLEAN     NOT NULL DEFAULT FALSE,
    processed_at   TIMESTAMPTZ
);
