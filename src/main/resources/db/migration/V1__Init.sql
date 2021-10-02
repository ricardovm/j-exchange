CREATE TABLE transaction (
    id BIGINT NOT NUlL PRIMARY KEY,
    user_id VARCHAR NOT NULL,
    base_currency VARCHAR NOT NULL,
    value NUMERIC(15, 5) NOT NULL,
    target_value NUMERIC(15, 5) NOT NULL,
    target_currency VARCHAR NOT NULL,
    exchange_rate NUMERIC(15, 5) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX transaction_user_timestamp ON transaction (user_id, timestamp);