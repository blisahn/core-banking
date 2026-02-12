package com.devblo.common.result;

import java.util.Objects;
import java.util.function.Function;

public sealed interface Result<T> permits Success, Failure {

    boolean isSuccess();
    default boolean isFailure() {
        return !isSuccess();
    }
    T getValue();
    String getError();

    default <R> Result<R> map(Function<T, R> mapper) {
        if (isSuccess()) {
            return Result.success(mapper.apply(getValue()));
        } else {
            return Result.failure(getError());
        }
    }

    default T orElse(T other) {
        return isSuccess() ? getValue() : other;
    }

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static Result<Void> success() {
        return new Success<>(null);
    }

    static <T> Result<T> failure(String error) {
        Objects.requireNonNull(error, "Error message cannot be null");
        return new Failure<>(error);
    }
}

record Success<T>(T value) implements Result<T> {
    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getError() {
        throw new IllegalStateException("Cannot get error from a successful result");
    }
}

record Failure<T>(String error) implements Result<T> {
    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T getValue() {
        throw new IllegalStateException("Cannot get value from a failed result: " + error);
    }

    @Override
    public String getError() {
        return error;
    }
}

