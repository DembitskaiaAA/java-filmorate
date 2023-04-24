package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreStorage {
    Collection<Genre> getAllGenre();

    Genre getGenre(int id);

    void addFilmsGenres(Film film);

    void removeFilmsGenres(Film film);

    Set<Genre> getFilmsGenres(Long id);

}

