package com.devblo.common;

public sealed interface Result<T> permits Success, Failure {

    boolean isSuccess();
    T getValue();
    String getError();

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(String error) {
        return new Failure<>(error);
    }
}
 record Success<T>(T value) implements Result<T> {
    public boolean isSuccess() { return true; }
    public T getValue() { return value; }
    public String getError() { return null; }
}

 record Failure<T>(String error) implements Result<T> {
    public boolean isSuccess() { return false; }
    public T getValue() { return null; }
    public String getError() { return error; }
}