package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Collection<Mpa> getAllMpa();

    Mpa getMpa(int id);

    void addFilmsMpa(Film film);

    void removeFilmsMpa(Film film);

    Mpa getFilmsMpa(Long id);
}
