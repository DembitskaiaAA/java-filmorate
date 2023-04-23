package ru.yandex.practicum.filmorate.exception;

public class FriendNotFoundException extends RuntimeException {
    public FriendNotFoundException(String msg) {
        super(msg);
    }
}
