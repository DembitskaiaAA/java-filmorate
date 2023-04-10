package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public String addLike(Long filmId, Long userId) {
        Map<Long, Film> films = filmStorage.getFilms();
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException(
                    String.format("При попытке поставить лайк произошла ошибка: " +
                            "фильму с id %s нельзя поставить лайк, так как он не добавлен", filmId));
        }
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(
                    String.format("При попытке поставить лайк произошла ошибка: " +
                            "пользователь с id %s не может поставить лайк, так как он не добавлен", userId));
        }
        Film film = films.get(filmId);
        film.getLikes().add(userId);
        return String.format("Пользователь с id: %d поставил лайк фильму %s", userId, film.getName());
    }

    public String deleteLike(Long filmId, Long userId) {
        Map<Long, Film> films = filmStorage.getFilms();
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException(
                    String.format("При попытке удалить лайк произошла ошибка: " +
                            "фильму с id %s нельзя удалить лайк, так как он не добавлен", filmId));
        }
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(
                    String.format("При попытке удалить лайк произошла ошибка: " +
                            "пользователь с id %s не может удалить лайк, так как он не добавлен", userId));
        }
        Film film = films.get(filmId);
        film.getLikes().remove(userId);
        return String.format("Пользователь с id: %d удалил лайк с фильма %s", userId, film.getName());
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
