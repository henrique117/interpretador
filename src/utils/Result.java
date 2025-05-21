package utils;

public class Result<T> {
    private final T value;
    private final String error;

    private Result(T value, String error) {
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(null, message);
    }

    public boolean isOk() {
        return error == null;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return error;
    }
}
