package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film postFilm(Film film);

    Film updateFilm(Film film);

    String deleteFilm(Long id);

    Film getFilm(Long id);

    Map<Long, Film> getFilms();

    boolean validateFilm(Long filmId);

}
