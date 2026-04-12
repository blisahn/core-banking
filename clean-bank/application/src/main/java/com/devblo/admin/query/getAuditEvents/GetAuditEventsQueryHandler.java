package com.devblo.admin.query.getAuditEvents;

import com.devblo.audit.AuditEventSummary;
import com.devblo.audit.repository.IAuditEventReadRepository;
import com.devblo.common.IQueryHandler;
import com.devblo.common.PagedResult;
import com.devblo.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAuditEventsQueryHandler implements IQueryHandler<GetAuditEventsQuery, Result<PagedResult<AuditEventSummary>>> {

    private final IAuditEventReadRepository repository;

    @Override
    @Transactional
    public Result<PagedResult<AuditEventSummary>> handle(GetAuditEventsQuery query) {
        var result = repository.findByFilters(
                query.aggregateType(),
                query.severity(),
                query.from(),
                query.to(),
                query.page(),
                query.size()
        );
        return Result.success(result);

    }
}
