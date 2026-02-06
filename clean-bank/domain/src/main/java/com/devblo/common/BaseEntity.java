package com.devblo.common;

import java.time.Instant;
import java.util.Objects;

public abstract class BaseEntity<ID> {
    private ID id;
    private Instant createdAt;
    private Instant updatedAt;

    public BaseEntity(ID id) {
        this.id = Objects.requireNonNull(id, "Id cannot be null");
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public BaseEntity(ID id, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "Id cannot be null");
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    protected void markUpdated() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
