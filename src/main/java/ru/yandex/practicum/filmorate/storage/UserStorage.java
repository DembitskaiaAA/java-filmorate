package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Collection<User> getAllUsers();

    User postUser(User user);

    User updateUser(User user);

    String deleteUser(Long id);

    User getUser(Long id);

    Map<Long, User> getUsers();
}
