CREATE TABLE audit_events
(
    id             UUID PRIMARY KEY,
    event_type     VARCHAR(100)             NOT NULL,                -- "AccountOpenedEvent", "MoneyDepositedEvent" vb.
    aggregate_type VARCHAR(50)              NOT NULL,                -- "account", "customer", "transaction"
    aggregate_id   UUID                     NOT NULL,
    actor_id       UUID,                                             -- işlemi yapan userId (JWT'den, nullable — seed/system events)
    actor_role     VARCHAR(20),                                      -- ADMIN, EMPLOYEE, CUSTOMER
    summary        VARCHAR(500)             NOT NULL,                -- insan-okunabilir özet
    severity       VARCHAR(20)              NOT NULL DEFAULT 'INFO', -- INFO, WARNING, CRITICAL
    payload        TEXT,                                             -- ham event JSON
    occurred_on    TIMESTAMP WITH TIME ZONE NOT NULL
);

create index idx_audit_events_occurred_on ON audit_events (occurred_on DESC);
create index idx_audit_events_aggregate ON audit_events (aggregate_type, aggregate_id);
create index idx_audit_events_severity on audit_events(severity, occurred_on desc);