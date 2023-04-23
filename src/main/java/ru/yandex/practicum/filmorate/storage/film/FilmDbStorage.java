package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MpaStorage mpaStorage, LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQueryFilms = "SELECT * FROM FILM";
        return jdbcTemplate.query(sqlQueryFilms, this::mapRowToFilm);

    }

    @Override
    public Film postFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsertFilm = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILM")
                .usingGeneratedKeyColumns("FILM_ID");
        film.setId(simpleJdbcInsertFilm.executeAndReturnKey(film.toMap()).longValue());

        genreStorage.addFilmsGenres(film);
        film.setGenres(genreStorage.getFilmsGenres(film.getId()));

        mpaStorage.addFilmsMpa(film);
        film.setMpa(mpaStorage.getFilmsMpa(film.getId()));

        jdbcTemplate.update(
                "INSERT INTO LIKES (LIKES_FILM_ID) VALUES (?)", film.getId());

        log.info("Фильм {} добавлен", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!validateFilm(film.getId())) {
            throw new FilmNotFoundException(String.format("При обновлении фильма обнаружена ошибка: фильм с id %d отсутствует", film.getId()));
        }
        jdbcTemplate.update(
                "UPDATE FILM SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?," +
                        " DURATION = ?, RATE = ? WHERE FILM_ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getId());

        genreStorage.removeFilmsGenres(film);
        genreStorage.addFilmsGenres(film);
        film.setGenres(genreStorage.getFilmsGenres(film.getId()));

        mpaStorage.removeFilmsMpa(film);
        mpaStorage.addFilmsMpa(film);
        film.setMpa(mpaStorage.getFilmsMpa(film.getId()));


        log.info("Фильм {} обновлен", film);
        return film;

    }

    @Override
    public String deleteFilm(Long id) {
        if (!validateFilm(id)) {
            throw new FilmNotFoundException(String.format("При удалении фильма обнаружена ошибка: фильм с id %d отсутствует", id));
        }
        String sqlQueryDeleteFilm = "DELETE from FILM where FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDeleteFilm, id);
        log.info("Фильм с id {} удален", id);
        return String.format("Фильм с id: %s удален", id);
    }

    @Override
    public Film getFilm(Long id) {
        if (!validateFilm(id)) {
            throw new FilmNotFoundException(String.format("При получении фильма обнаружена ошибка: фильм с id %d отсутствует", id));
        }
/*        String queryFilmResult = "SELECT FILM.*,RATING.RATING_MPA_ID, RATING.RATING_MPA_NAME, GENRE.GENRE_ID, GENRE.GENRE_NAME " +
                "FROM FILM " +
                "JOIN FILM_RATING ON FILM.FILM_ID = FILM_RATING.FILM_RATING_FILM_ID " +
                "JOIN RATING ON FILM_RATING.FILM_RATING_MPA_ID = RATING.RATING_MPA_ID " +
                "JOIN FILM_CATEGORY ON FILM.FILM_ID = FILM_CATEGORY.FILM_CATEGORY_FILM_ID " +
                "JOIN GENRE ON FILM_CATEGORY.FILM_CATEGORY_GENRE_ID = GENRE.GENRE_ID " +
                "WHERE FILM.FILM_ID = ?;";

        return jdbcTemplate.queryForObject(queryFilmResult, this::mapRowToFilm, id);*/

        String queryFilmResult = "SELECT * FROM FILM WHERE FILM.FILM_ID = ?;";
        return jdbcTemplate.queryForObject(queryFilmResult, this::mapRowToFilm, id);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return  new Film(
                rs.getLong("FILM_ID"),
                likeStorage.getFilmsLikes(rs.getLong("FILM_ID")),
                rs.getString("FILM_NAME"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getString("DESCRIPTION"),
                rs.getInt("DURATION"),
                rs.getInt("RATE"),
                mpaStorage.getFilmsMpa(rs.getLong("FILM_ID")),
                genreStorage.getFilmsGenres(rs.getLong("FILM_ID"))
        );
    }

    @Override
    public boolean validateFilm(Long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILM WHERE FILM_ID = ?", filmId);
        return filmRows.next();
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }
}
