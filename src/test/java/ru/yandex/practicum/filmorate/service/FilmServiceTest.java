package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmServiceTest {

    @Autowired
    FilmService filmService;
    @Autowired
    LikeStorage likeStorage;
    @Autowired
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;

    @Autowired
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void testAddLike() {
        Long filmId = 1L;
        Long userId = 1L;

        Film film = new Film("Title 1", LocalDate.of(2022, 1, 1),
                "Description 1", 120, 7, new Mpa(1, "G"),
                Set.of(new Genre(1, "Комедия")));
        User user = new User("john.smith@example.com",
                "johnsmith",
                "John Smith",
                LocalDate.of(1990, 5, 15));

        filmStorage.postFilm(film);
        userStorage.postUser(user);
        String expectedResult = "Пользователь с id: 1 поставил лайк фильму с id: 1";
        String result = filmService.addLike(filmId, userId);
        assertEquals(expectedResult, result);

    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void testDeleteLike() {
        Long filmId = 1L;
        Long userId = 1L;

        Film film = new Film("Title 1", LocalDate.of(2022, 1, 1),
                "Description 1", 120, 7, new Mpa(1, "G"),
                Set.of(new Genre(1, "Комедия")));
        User user = new User("john.smith@example.com",
                "johnsmith",
                "John Smith",
                LocalDate.of(1990, 5, 15));

        filmStorage.postFilm(film);
        userStorage.postUser(user);

        String expectedResult1 = "Пользователь с id: 1 поставил лайк фильму с id: 1";
        String result1 = filmService.addLike(userId, filmId);
        assertEquals(expectedResult1, result1);

        String expectedResult2 = "Пользователь с id: 1 удалил лайк к фильму с id: 1";
        String result2 = filmService.deleteLike(userId, filmId);
        assertEquals(expectedResult2, result2);
        assertEquals(0, film.getLikes().size());

    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void testGetPopularFilm() throws FilmNotFoundException {
        Film film1 = new Film("Title 1", LocalDate.of(2022, 1, 1),
                "Description 1", 120, 7, new Mpa(1, "G"),
                Set.of(new Genre(1, "Комедия")));
        Film film2 = new Film("Title 2", LocalDate.of(2022, 2, 2),
                "Description 2", 90, 6, new Mpa(2, "PG-13"),
                Set.of(new Genre(2, "Драма")));
        Film film3 = new Film("Title 3", LocalDate.of(2022, 3, 3),
                "Description 3", 150, 8, new Mpa(1, "G"),
                Set.of(new Genre(2, "Драма")));

        User user = new User("john.smith@example.com",
                "johnsmith", "John Smith", LocalDate.of(1990, 5, 15));
        User user2 = new User("nick.smith@example.com",
                "nicksmith", "Nick Smith", LocalDate.of(1990, 5, 15));

        filmStorage.postFilm(film1);
        filmStorage.postFilm(film2);
        filmStorage.postFilm(film3);

        userStorage.postUser(user);
        userStorage.postUser(user2);

        String result = filmService.addLike(film1.getId(), user.getId());
        String result2 = filmService.addLike(film1.getId(), user2.getId());

        assertEquals(3, likeStorage.getPopular(10).size());
        String expectedResult = "Пользователь с id: 1 поставил лайк фильму с id: 1";
        String expectedResult2 = "Пользователь с id: 2 поставил лайк фильму с id: 1";

        assertEquals(expectedResult, result);
        assertEquals(expectedResult2, result2);

    }

}