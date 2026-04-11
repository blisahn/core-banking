package com.devblo.infrastructure.persistence.audit;

import com.devblo.audit.AuditEventDetail;
import com.devblo.audit.AuditEventSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface IAuditEventJpaRepository extends JpaRepository<AuditEventEntity, UUID> {

    @Query("""
            SELECT new com.devblo.audit.AuditEventSummary(
                e.id, e.eventType, e.aggregateType, e.aggregateId,
                e.actorId, e.actorRole, e.summary, e.severity, e.occurredOn
            )
            FROM AuditEventEntity e
            ORDER BY e.occurredOn DESC
            """)
    Page<AuditEventSummary> findAllProjected(Pageable pageable);

    @Query("""
            SELECT new com.devblo.audit.AuditEventSummary(
                e.id, e.eventType, e.aggregateType, e.aggregateId,
                e.actorId, e.actorRole, e.summary, e.severity, e.occurredOn
            )
            FROM AuditEventEntity e
            WHERE (:aggregateType IS NULL OR e.aggregateType = :aggregateType)
              AND (:severity IS NULL OR e.severity = :severity)
              AND (CAST(:from AS timestamp) IS NULL OR e.occurredOn >= :from)
              AND (CAST(:to AS timestamp) IS NULL OR e.occurredOn <= :to)
            ORDER BY e.occurredOn DESC
            """)
    Page<AuditEventSummary> findByFilters(
            @Param("aggregateType") String aggregateType,
            @Param("severity") String severity,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable
    );

    @Query("""
            SELECT new com.devblo.audit.AuditEventDetail(
                e.id, e.eventType, e.aggregateType, e.aggregateId,
                e.actorId, e.actorRole, e.summary, e.severity, e.payload, e.occurredOn
            )
            FROM AuditEventEntity e
            WHERE e.id = :id
            """)
    Optional<AuditEventDetail> findDetailById(@Param("id") UUID id);
}
