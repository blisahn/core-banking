package com.devblo.audit.repository;

import com.devblo.audit.AuditEventDetail;
import com.devblo.audit.AuditEventSummary;
import com.devblo.common.PagedResult;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface IAuditEventReadRepository {

    PagedResult<AuditEventSummary> findAll(int page, int size);

    PagedResult<AuditEventSummary> findByFilters(
            String aggregateType,
            String severity,
            Instant from,
            Instant to,
            int page, int size
    );

    Optional<AuditEventDetail> findById(UUID id);
}
