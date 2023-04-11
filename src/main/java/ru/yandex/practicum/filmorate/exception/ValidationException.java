package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends Throwable {
    public ValidationException(String msg) {
        super(msg);
    }

    public String getInfo() {
        return getMessage();
    }
}
