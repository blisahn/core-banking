package com.devblo.admin.query.getAuditEvents;

import com.devblo.audit.AuditEventSummary;
import com.devblo.common.IQuery;
import com.devblo.common.PagedResult;
import com.devblo.common.result.Result;

import java.time.Instant;

public record GetAuditEventsQuery(
        String aggregateType,
        String severity,
        Instant from,
        Instant to,
        int page,
        int size
) implements IQuery<Result<PagedResult<AuditEventSummary>>> {
}
