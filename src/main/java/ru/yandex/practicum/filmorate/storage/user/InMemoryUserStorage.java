package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private long id = 0;
    FilmStorage filmStorage;

    @Autowired
    InMemoryUserStorage(@Qualifier("InMemoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

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
    public String deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(
                    String.format("При попытке удалить пользователя произошла ошибка: " +
                            "пользователь с id: %s не добавлен в базу", id));
        }
        //Удаляю пользователя из друзей у его друзей
        users.get(id).getFriends().forEach(x -> users.get(x).getFriends().remove(id));
        //Удаляю лайки пользователя с фильмов
        filmStorage.getAllFilms().stream()
                .filter(x -> x.getLikes().contains(id))
                .forEach(x -> x.getLikes().remove(id));
        users.remove(id);
        return String.format("Пользователь с id: %s удален", id);
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

    @Override
    public boolean validateUser(Long userId) {
        return false;
    }
}
