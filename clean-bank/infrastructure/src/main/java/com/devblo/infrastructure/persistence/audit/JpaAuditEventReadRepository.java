package com.devblo.infrastructure.persistence.audit;

import com.devblo.audit.AuditEventDetail;
import com.devblo.audit.AuditEventSummary;
import com.devblo.audit.repository.IAuditEventReadRepository;
import com.devblo.common.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaAuditEventReadRepository implements IAuditEventReadRepository {

    private final IAuditEventJpaRepository jpaRepository;

    @Override
    public PagedResult<AuditEventSummary> findAll(int page, int size) {
        Page<AuditEventSummary> result = jpaRepository.findAllProjected(PageRequest.of(page, size));
        return toPagedResult(result);
    }

    @Override
    public PagedResult<AuditEventSummary> findByFilters(String aggregateType, String severity,
                                                         Instant from, Instant to,
                                                         int page, int size) {
        Page<AuditEventSummary> result = jpaRepository.findByFilters(
                aggregateType, severity, from, to, PageRequest.of(page, size));
        return toPagedResult(result);
    }

    @Override
    public Optional<AuditEventDetail> findById(UUID id) {
        return jpaRepository.findDetailById(id);
    }

    private PagedResult<AuditEventSummary> toPagedResult(Page<AuditEventSummary> page) {
        return new PagedResult<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
