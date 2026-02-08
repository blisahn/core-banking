package com.devblo.common;

public sealed interface Result<T> permits Success, Failure {


    boolean isSuccess();

    T getValue();

    String getMessage();

    int getHttpStatusCode();

    static <T> Result<T> ok(T value) {
        return new Success<>(value, 200, null);
    }

    static <T> Result<T> created(T value) {
        return new Success<>(value, 201, null);
    }

    static <T> Result<T> accepted(T value) {
        return new Success<>(value, 202, null);
    }



    static <T> Result<T> noContent() {
        return new Success<>(null, 204, null);
    }

    static <T> Result<T> badRequest(String message) {
        return new Failure<>(400, message);
    }

    static <T> Result<T> notFound(String message) {
        return new Failure<>(404, message);
    }

    static <T> Result<T> unexpected(String message) {
        return new Failure<>(500, message);
    }

    static <T> Result<T> conflict(String message) {
        return new Failure<>(409, message);
    }

    static <T> Result<T> unprocessable(String message) {
        return new Failure<>(422, message);
    }

}

record Success<T>(T value, int httpStatusCode, String message) implements Result<T> {


    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}

record Failure<T>(int httpStatusCode, String message) implements Result<T> {

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T getValue() {
        return null;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}

