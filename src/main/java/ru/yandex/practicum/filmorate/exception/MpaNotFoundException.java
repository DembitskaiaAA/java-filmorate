package ru.yandex.practicum.filmorate.exception;

public class MpaNotFoundException extends RuntimeException {
    public MpaNotFoundException(String msg) {
        super(msg);
    }
}