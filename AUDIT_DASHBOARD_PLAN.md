# Admin Audit Event Dashboard + SSE Real-time Updates

## Context

Admin kullanıcının sistemdeki tüm domain olaylarını (hesap açma, para yatırma/çekme, müşteri kaydı, durum değişiklikleri vb.) gerçek zamanlı izleyebileceği bir dashboard ekliyoruz. Mevcut outbox altyapısı sadece RabbitMQ delivery için — audit log ayrı bir concern olmalı, kendi tablosu ve read model'iyle.

SSE (Server-Sent Events) ile admin tarayıcısına anlık push yapılacak.

---

## Adım 1: Flyway Migration — `audit_events` tablosu

**Dosya:** `infrastructure/src/main/resources/db/migration/V4__create_audit_events_table.sql`

```sql
CREATE TABLE audit_events (
    id              UUID PRIMARY KEY,
    event_type      VARCHAR(100)  NOT NULL,   -- "AccountOpenedEvent", "MoneyDepositedEvent" vb.
    aggregate_type  VARCHAR(50)   NOT NULL,   -- "account", "customer", "transaction"
    aggregate_id    UUID          NOT NULL,
    actor_id        UUID,                     -- işlemi yapan userId (JWT'den, nullable — seed/system events)
    actor_role      VARCHAR(20),              -- ADMIN, EMPLOYEE, CUSTOMER
    summary         VARCHAR(500)  NOT NULL,   -- insan-okunabilir özet
    severity        VARCHAR(20)   NOT NULL DEFAULT 'INFO', -- INFO, WARNING, CRITICAL
    payload         TEXT,                     -- ham event JSON
    occurred_on     TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_audit_events_occurred_on ON audit_events(occurred_on DESC);
CREATE INDEX idx_audit_events_aggregate ON audit_events(aggregate_type, aggregate_id);
CREATE INDEX idx_audit_events_severity ON audit_events(severity, occurred_on DESC);
```

---

## Adım 2: Domain Katmanı — Read Model + Repository Interface

**2a) `domain/src/.../audit/AuditEventSummary.java`** — read model record:
```
record AuditEventSummary(
    UUID id,
    String eventType,
    String aggregateType,
    UUID aggregateId,
    UUID actorId,        // nullable
    String actorRole,    // nullable
    String summary,
    String severity,     // "INFO" | "WARNING" | "CRITICAL"
    Instant occurredOn
)
```

**2b) `domain/src/.../audit/repository/IAuditEventReadRepository.java`**:
```
PagedResult<AuditEventSummary> findAll(int page, int size);
PagedResult<AuditEventSummary> findByFilters(
    String aggregateType,    // nullable = tümü
    String severity,         // nullable = tümü
    Instant from,            // nullable
    Instant to,              // nullable
    int page, int size
);
```

---

## Adım 3: Infrastructure — Entity, Mapper, JPA Adapter

**3a) `AuditEventEntity.java`** — `infrastructure/src/.../persistence/audit/`
- JPA `@Entity` table `audit_events`
- Alanlar: id (UUID), eventType, aggregateType, aggregateId, actorId, actorRole, summary, severity, payload (TEXT), occurredOn

**3b) `AuditEventEntityMapper.java`** — sadece `toSummary(AuditEventEntity) → AuditEventSummary` (bu read-only, toEntity/toDomain gerekmez — yazma AuditEventPersistenceService üzerinden)

**3c) `AuditEventJpaRepository.java`** — Spring Data JPA, custom query'ler:
- `findAllByOrderByOccurredOnDesc(Pageable)` — sayfalı, en yeniden başlayarak
- `findByFilters(aggregateType, severity, from, to, Pageable)` — JPQL veya Specification ile filtreleme

**3d) `JpaAuditEventReadRepositoryAdapter.java`** — `IAuditEventReadRepository` implementasyonu

---

## Adım 4: Infrastructure — AuditEventPersistenceService

**Dosya:** `infrastructure/src/.../events/audit/AuditEventPersistenceService.java`

- `@Transactional` `saveAuditEvent(BaseDomainEvent, aggregateType, aggregateId)` methodu
- `SecurityContextHolder.getContext().getAuthentication()` üzerinden `actorId` ve `actorRole` çeker (nullable — system eventleri için)
- Event tipine göre `severity` belirler:
  - CRITICAL: `CustomerStatusChangedEvent` (CLOSED), hesap kapatma
  - WARNING: `CustomerStatusChangedEvent` (SUSPENDED), hesap dondurma
  - INFO: diğer her şey
- Event tipine göre insan-okunabilir `summary` üretir (switch/pattern matching):
  - `AccountOpenedEvent` → "Yeni {accountType} hesap açıldı (müşteri: {customerId})"
  - `MoneyDepositedEvent` → "{amount} {currency} yatırıldı (hesap: {accountId})"
  - `MoneyWithdrawEvent` → "{amount} {currency} çekildi (hesap: {accountId})"
  - `CustomerRegisteredEvent` → "Yeni müşteri kaydı: {firstName} {lastName}"
  - `CustomerStatusChangedEvent` → "Müşteri durumu değişti: {old} → {new}"
  - vb.
- Event'i JSON'a serialize eder (`payload` alanı için) — mevcut `OutboxEventService` ile aynı `ObjectMapper` pattern'i

---

## Adım 5: Infrastructure — AuditEventListener

**Dosya:** `infrastructure/src/.../events/listeners/AuditEventListener.java`

- `@Component`, `AuditEventPersistenceService`'i inject eder
- Tüm domain eventlerini dinler: `@TransactionalEventListener(phase = BEFORE_COMMIT)`
- Her event tipi için bir method (mevcut `AccountEventListener`/`CustomerEventListener` pattern'iyle aynı yapı)
- `auditEventPersistenceService.saveAuditEvent(event, aggregateType, aggregateId)` çağırır
- **NOT:** Mevcut outbox listener'lara dokunmuyoruz — audit ayrı bir concern

---

## Adım 6: Infrastructure — SSE Altyapısı

**6a) `SseEmitterManager.java`** — `infrastructure/src/.../events/sse/`
```java
@Component
public class SseEmitterManager {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter create(long timeout) { ... }  // emitter oluştur, listeye ekle, cleanup callback'leri ayarla
    public void broadcast(AuditEventSummary event) { ... }  // tüm emitter'lara gönder, dead olanları temizle
}
```
- `CopyOnWriteArrayList` thread-safe erişim için
- `broadcast()` dead emitter'ları otomatik temizler (send sırasında IOException → remove)
- Emitter timeout: 30 dakika (admin uzun süre açık tutabilir)

**6b) `SseEventNotifier.java`** — `infrastructure/src/.../events/sse/`
```java
@Component
public class SseEventNotifier {
    // @TransactionalEventListener(phase = AFTER_COMMIT) — DB commit SONRASI SSE push
    // Her domain event tipi için method
    // Event → AuditEventSummary'e dönüştür → sseEmitterManager.broadcast(summary)
}
```
- **Kritik:** `AFTER_COMMIT` kullanılmalı (BEFORE_COMMIT değil) — aksi halde rollback olan bir event SSE'ye push edilir
- `AuditEventPersistenceService` ile aynı summary/severity mantığını kullanır (ortak bir utility class çıkar veya service'i inject et)

**Ortak utility — `AuditEventDescriptor.java`:**
- Static methods: `resolveSeverity(BaseDomainEvent)`, `buildSummary(BaseDomainEvent)`, `resolveAggregateType(BaseDomainEvent)`
- Hem `AuditEventPersistenceService` hem `SseEventNotifier` tarafından kullanılır — DRY

---

## Adım 7: Application — Query

**7a) `application/src/.../admin/query/getAuditEvents/GetAuditEventsQuery.java`**
```java
public record GetAuditEventsQuery(
    String aggregateType,  // nullable
    String severity,       // nullable
    Instant from,          // nullable
    Instant to,            // nullable
    int page,
    int size
) implements IQuery<PagedResult<AuditEventSummary>> {}
```

**7b) `GetAuditEventsQueryHandler.java`** — `IAuditEventReadRepository.findByFilters(...)` çağırır

---

## Adım 8: API — Controller + SSE Endpoint

**Dosya:** `AdminController.java`'ya yeni endpointler eklenir (mevcut dosya)

**8a) REST endpoint — geçmiş eventler:**
```java
@GetMapping("/events")
public ResponseEntity<ApiResponse<PagedResult<AuditEventSummary>>> getAuditEvents(
    @RequestParam(required = false) String aggregateType,
    @RequestParam(required = false) String severity,
    @RequestParam(required = false) Instant from,
    @RequestParam(required = false) Instant to,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
)
```

**8b) SSE endpoint — real-time stream:**
```java
@GetMapping(value = "/events/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter streamEvents() {
    return sseEmitterManager.create(30 * 60 * 1000L);  // 30 dk timeout
}
```

**8c) SecurityConfig güncelleme:**
- `/api/admin/events/stream` zaten `/api/admin/**` → `ROLE_ADMIN` kuralı altında, ekstra bir şey gerekmez
- CORS: SSE streaming için mevcut CORS config yeterli (same-origin + credentials)

---

## Adım 9: Frontend — Types + Service

**9a) `src/types/index.ts`** — yeni tipler:
```typescript
export type AuditSeverity = "INFO" | "WARNING" | "CRITICAL";

export interface AuditEvent {
  id: string;
  eventType: string;
  aggregateType: string;
  aggregateId: string;
  actorId: string | null;
  actorRole: string | null;
  summary: string;
  severity: AuditSeverity;
  occurredOn: string;
}
```

**9b) `src/lib/services/admin.ts`** — yeni method:
```typescript
getAuditEvents: (params: {
  page?: number; size?: number;
  aggregateType?: string; severity?: string;
  from?: string; to?: string;
}) => api.get<PagedResult<AuditEvent>>(`/admin/events?${buildQuery(params)}`),
```

---

## Adım 10: Frontend — SSE Hook

**Dosya:** `src/lib/hooks/useEventStream.ts`

- `fetch` API ile SSE bağlantısı (`EventSource` custom header desteklemez, JWT gerektiği için `fetch` + `ReadableStream` kullanılır)
- `Authorization: Bearer <token>` header'ı gönderir
- SSE text stream'i parse eder (`data:` satırlarını JSON'a çevirir)
- Reconnect mantığı: bağlantı koparsa 3 saniye sonra tekrar dener
- Component unmount'ta cleanup (AbortController)
- Return: `{ events: AuditEvent[], isConnected: boolean }`

```typescript
export function useEventStream(enabled: boolean) {
  const [events, setEvents] = useState<AuditEvent[]>([]);
  const [isConnected, setIsConnected] = useState(false);
  // fetch + ReadableStream + TextDecoder ile SSE parse
  // Yeni event geldiğinde setEvents(prev => [newEvent, ...prev].slice(0, 50))  // son 50 canlı event
  return { events, isConnected };
}
```

---

## Adım 11: Frontend — Events Sayfası

**Dosya:** `src/app/dashboard/events/page.tsx`

- `RoleGuard allowedRoles={["ADMIN"]}`
- İki bölüm:
  1. **Canlı Akış (üst):** `useEventStream` hook'u ile gelen son eventler — yeşil "bağlı" / kırmızı "bağlantı kesildi" göstergesi
  2. **Geçmiş (alt):** Sayfalanmış tablo + filtreler

**Filtreler:**
- Aggregate type: `<Select>` — Tümü / Account / Customer / Transaction
- Severity: `<Select>` — Tümü / INFO / WARNING / CRITICAL
- Tarih aralığı: iki date input (from / to)

**Tablo sütunları:** Zaman, Severity (renk kodlu badge), Olay Tipi, Özet, Aggregate ID (kısaltılmış), Actor

**Severity renk kodları:**
- INFO → slate/neutral badge
- WARNING → amber/yellow badge
- CRITICAL → rose/red badge

---

## Adım 12: Frontend — Navigation Güncelleme

**Dosya:** `src/app/dashboard/layout.tsx`

ADMIN sidebar navigation'a "Events" linki eklenir:
```
{ label: "Events", href: "/dashboard/events", icon: Activity }
```

---

## Adım 13: AdminDashboard'a Event İstatistiği

**Dosya:** `src/components/dashboard/AdminDashboard.tsx`

- Mevcut 3 StatsCard'a 4. kart eklenir: "Recent Events" (son 24 saatteki event sayısı)
- QuickLink'lere "View Events" eklenir → `/dashboard/events`

---

## Dosya Özeti

### Yeni dosyalar (Backend — 10):
1. `infrastructure/src/main/resources/db/migration/V4__create_audit_events_table.sql`
2. `domain/src/.../audit/AuditEventSummary.java`
3. `domain/src/.../audit/repository/IAuditEventReadRepository.java`
4. `infrastructure/src/.../persistence/audit/AuditEventEntity.java`
5. `infrastructure/src/.../persistence/audit/AuditEventEntityMapper.java`
6. `infrastructure/src/.../persistence/audit/AuditEventJpaRepository.java`
7. `infrastructure/src/.../persistence/audit/JpaAuditEventReadRepositoryAdapter.java`
8. `infrastructure/src/.../events/audit/AuditEventPersistenceService.java`
9. `infrastructure/src/.../events/audit/AuditEventDescriptor.java` (ortak severity/summary utility)
10. `infrastructure/src/.../events/listeners/AuditEventListener.java`

### Yeni dosyalar (SSE — 2):
11. `infrastructure/src/.../events/sse/SseEmitterManager.java`
12. `infrastructure/src/.../events/sse/SseEventNotifier.java`

### Yeni dosyalar (Application — 2):
13. `application/src/.../admin/query/getAuditEvents/GetAuditEventsQuery.java`
14. `application/src/.../admin/query/getAuditEvents/GetAuditEventsQueryHandler.java`

### Yeni dosyalar (Frontend — 2):
15. `clean-bank-ui/src/lib/hooks/useEventStream.ts`
16. `clean-bank-ui/src/app/dashboard/events/page.tsx`

### Değiştirilecek dosyalar (5):
17. `api/src/.../controller/admin/AdminController.java` — 2 yeni endpoint
18. `clean-bank-ui/src/types/index.ts` — AuditEvent + AuditSeverity tipleri
19. `clean-bank-ui/src/lib/services/admin.ts` — getAuditEvents methodu
20. `clean-bank-ui/src/app/dashboard/layout.tsx` — ADMIN nav'a Events linki
21. `clean-bank-ui/src/components/dashboard/AdminDashboard.tsx` — Events stats + quick link

---

## Doğrulama

1. `cd clean-bank && mvn compile` — derleme hatası yok
2. `cd clean-bank && mvn test` — mevcut testler kırılmadı
3. Backend'i çalıştır (`mvn spring-boot:run -pl api`) → Flyway V4 migration uygulandı mı kontrol et
4. Frontend'i çalıştır (`npm run dev`) → Admin olarak giriş yap
5. `/dashboard/events` sayfasına git → SSE bağlantısı kuruldu mu (yeşil gösterge)
6. Başka bir tarayıcıda müşteri olarak giriş yap → para yatır, hesap aç vb.
7. Admin events sayfasında anlık olarak olayların göründüğünü doğrula
8. Filtreler (aggregate type, severity) çalışıyor mu test et
9. Sayfalama çalışıyor mu test et
10. Swagger UI'da `/api/admin/events` endpoint'ini kontrol et
