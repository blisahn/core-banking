-- Optimistic locking: add version columns to all entities
ALTER TABLE accounts ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE customers ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE transactions ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
