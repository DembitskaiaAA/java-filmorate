package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MpaDbStorageTest {
    @Autowired
    FilmService filmService;
    @Autowired
    LikeStorage likeStorage;
    @Autowired
    FriendStorage friendStorage;
    @Autowired
    MpaStorage mpaStorage;
    @Autowired
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;

    @Autowired
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getAllMpaTest() {
        assertEquals(5, mpaStorage.getAllMpa().size());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getMpaTest() {
        Mpa mpaTest = mpaStorage.getMpa(2);
        assertEquals(new Mpa(2, "PG"), mpaTest);
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getMpaWrongIdTest() {
        assertThrows(MpaNotFoundException.class, () -> mpaStorage.getMpa(-2));
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void addFilmsMpaTest() {
        Film srcFilm = new Film("test film", LocalDate.of(2022, 4, 23),
                "description", 120, 8, new Mpa(2, "PG-13"),
                Set.of(new Genre(2, "Драма")));
        Film savedFilm = filmStorage.postFilm(srcFilm);

        assertEquals(srcFilm.getMpa(), savedFilm.getMpa());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void removeFilmsMpaTest() {
        Film srcFilm = new Film("test film", LocalDate.of(2022, 4, 23),
                "description", 120, 8, new Mpa(2, "PG-13"),
                Set.of(new Genre(2, "Драма")));
        Film updatedFilm = new Film(1L, "updated test film", LocalDate.of(2022, 4, 23),
                "updated description", 130, 9, new Mpa(1, "G"),
                Set.of(new Genre(2, "Драма")));

        filmStorage.postFilm(srcFilm);
        Film result = filmStorage.updateFilm(updatedFilm);
        assertEquals(updatedFilm.getMpa(), result.getMpa());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getFilmsMpaTest() {
        Film srcFilm = new Film("test film", LocalDate.of(2022, 4, 23),
                "description", 120, 8, new Mpa(2, "PG-13"),
                Set.of(new Genre(2, "Драма")));

        Film result = filmStorage.postFilm(srcFilm);
        assertEquals(new Mpa(2, "PG"), mpaStorage.getFilmsMpa(result.getId()));
    }
}