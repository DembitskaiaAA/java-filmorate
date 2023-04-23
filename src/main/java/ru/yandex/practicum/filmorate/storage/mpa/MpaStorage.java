package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface MpaStorage {
    Collection<Mpa> getAllMpa();

    Mpa getMpa(int id);

    void addFilmsMpa(Film film);

    void removeFilmsMpa(Film film);

    Mpa getFilmsMpa(Long id);
}
