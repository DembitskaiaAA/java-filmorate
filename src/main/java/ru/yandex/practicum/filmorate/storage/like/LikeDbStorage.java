package ru.yandex.practicum.filmorate.storage.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public LikeDbStorage(JdbcTemplate jdbcTemplate,
                         GenreStorage genreStorage,
                         MpaStorage mpaStorage,
                         @Lazy @Qualifier("FilmDbStorage") FilmStorage filmStorage,
                         @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        if (!userStorage.validateUser(userId)) {
            throw new UserNotFoundException(
                    String.format("При добавлении лайка обнаружена ошибка: пользователь с id %d отсутствует", userId));
        }
        if (!filmStorage.validateFilm(filmId)) {
            throw new FilmNotFoundException(
                    String.format("При добавлении лайка обнаружена ошибка: фильм с id %d отсутствует", filmId));
        }
        jdbcTemplate.update("INSERT INTO LIKES (LIKES_FILM_ID, LIKES_CLIENT_ID) VALUES (?, ?)", filmId, userId);
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}", userId, filmId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        if (!userStorage.validateUser(userId)) {
            throw new UserNotFoundException(
                    String.format("При удалении лайка обнаружена ошибка: пользователь с id %d отсутствует", userId));
        }
        if (!filmStorage.validateFilm(filmId)) {
            throw new FilmNotFoundException(
                    String.format("При удалении лайка обнаружена ошибка: фильм с id %d отсутствует", filmId));
        }
        jdbcTemplate.update("DELETE FROM LIKES WHERE LIKES_FILM_ID = ? AND LIKES_CLIENT_ID = ?", filmId, userId);
        log.info("Пользователь с id: {} удалил лайк фильму с id: {}", userId, filmId);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return jdbcTemplate.query("SELECT LIKES.LIKES_FILM_ID, COUNT(LIKES_CLIENT_ID), FILM.* " +
                "FROM LIKES " +
                "JOIN FILM ON LIKES.LIKES_FILM_ID = FILM.FILM_ID " +
                "GROUP BY LIKES_FILM_ID " +
                "ORDER BY COUNT(LIKES_CLIENT_ID) DESC " +
                "LIMIT ?", (rs, rowNum) -> new Film(
                rs.getLong("FILM_ID"),
                getFilmsLikes(rs.getLong("FILM_ID")),
                rs.getString("FILM_NAME"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getString("DESCRIPTION"),
                rs.getInt("DURATION"),
                rs.getInt("RATE"),
                mpaStorage.getFilmsMpa(rs.getLong("FILM_ID")),
                genreStorage.getFilmsGenres(rs.getLong("FILM_ID"))
        ), count);
    }

    @Override
    public Set<Long> getFilmsLikes(Long id) {
        return new HashSet<>(jdbcTemplate.query("SELECT LIKES.LIKES_FILM_ID, LIKES.LIKES_CLIENT_ID FROM LIKES " +
                        "WHERE LIKES.LIKES_FILM_ID = ?", (rs, rowNum) -> (
                        rs.getLong("LIKES.LIKES_CLIENT_ID")),
                id
        ));
    }
}
