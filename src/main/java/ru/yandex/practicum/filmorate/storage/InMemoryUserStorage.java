package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User postUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен в список", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Обнаружена ошибка: пользователь с id {} отсутствует", user.getId());
            throw new UserNotFoundException(String.format("Обнаружена ошибка: пользователь с id %s отсутствует",
                    user.getId()));
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь {} обновлен в списке", user.getName());
        return user;
    }

    @Override
    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            log.error("Обнаружена ошибка: пользователь с id {} отсутствует", id);
            throw new UserNotFoundException(String.format("Обнаружена ошибка: пользователь с id %s отсутствует", id));
        }
        return users.get(id);
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }
}
