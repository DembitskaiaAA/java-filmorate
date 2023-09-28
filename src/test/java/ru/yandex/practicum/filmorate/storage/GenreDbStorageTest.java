package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GenreDbStorageTest {
    @Autowired
    FilmService filmService;
    @Autowired
    LikeStorage likeStorage;
    @Autowired
    FriendStorage friendStorage;
    @Autowired
    MpaStorage mpaStorage;
    @Autowired
    GenreStorage genreStorage;

    @Autowired
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;

    @Autowired
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getAllGenreTest() {
        assertEquals(6, genreStorage.getAllGenre().size());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getGenreTest() {
        Genre genreTest = genreStorage.getGenre(2);
        assertEquals(new Genre(2, "Драма"), genreTest);
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getGenreWrongIdTest() {
        assertThrows(GenreNotFoundException.class, () -> genreStorage.getGenre(-2));
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void addFilmsGenresTest() {
        Film film = new Film("Title 1", LocalDate.of(2022, 1, 1),
                "Description 1", 120, 7, new Mpa(1, "G"),
                Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма")));
        filmStorage.postFilm(film);

        Set<Genre> genreResult = genreStorage.getFilmsGenres(1L);
        assertEquals((Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма"))), genreResult);
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void removeFilmsGenreTest() {
        Film srcFilm = new Film("test film", LocalDate.of(2022, 4, 23),
                "description", 120, 8, new Mpa(2, "PG-13"),
                Set.of(new Genre(2, "Драма")));
        Film updatedFilm = new Film(1L, "updated test film", LocalDate.of(2022, 4, 23),
                "updated description", 130, 9, new Mpa(1, "G"),
                Set.of(new Genre(1, "Комедия")));

        filmStorage.postFilm(srcFilm);
        Film result = filmStorage.updateFilm(updatedFilm);
        assertEquals(updatedFilm.getGenres(), result.getGenres());
    }

}
