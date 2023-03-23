package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film postFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(Long id);

    Map<Long, Film> getFilms();

}
