package com.devblo.common;

import java.util.List;

public record PagedResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public PagedResult {
        if (page < 0) throw new IllegalArgumentException("Page must be >= 0");
        if (size < 1) throw new IllegalArgumentException("Size must be >= 1");
    }

    public boolean hasNext() {
        return page + 1 < totalPages;
    }

    public boolean hasPrevious() {
        return page > 0;
    }
}
