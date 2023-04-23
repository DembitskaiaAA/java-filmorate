package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String sqlQueryMpa = "SELECT * FROM RATING;";
        return jdbcTemplate.query(sqlQueryMpa, this::mapRowToMpa);
    }

    @Override
    public Mpa getMpa(int id) {
        if (!validateMpa(id)) {
            throw new MpaNotFoundException(String.format("При получении mpa обнаружена ошибка: mpa с id %d отсутствует", id));
        }
        String queryMpaResult = "SELECT * FROM RATING WHERE RATING_MPA_ID = ?;";

        Mpa mpaResult = jdbcTemplate.queryForObject(queryMpaResult, this::mapRowToMpa, id);
        return mpaResult;
    }

    @Override
    public void addFilmsMpa(Film film) {
        String sqlQueryFilmRating = "insert into FILM_RATING(FILM_RATING_FILM_ID, FILM_RATING_MPA_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQueryFilmRating,
                film.getId(),
                film.getMpa().getId());
    }

    @Override
    public void removeFilmsMpa(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_RATING WHERE FILM_RATING_FILM_ID = ?", film.getId());
    }

    @Override
    public Mpa getFilmsMpa(Long id) {
        String queryMpaResult = "SELECT RATING.RATING_MPA_ID, RATING.RATING_MPA_NAME FROM FILM_RATING" +
                " JOIN RATING ON FILM_RATING.FILM_RATING_MPA_ID = RATING.RATING_MPA_ID" +
                " WHERE FILM_RATING.FILM_RATING_FILM_ID = ?" +
                " ORDER BY RATING.RATING_MPA_ID ";
        Mpa resultMpa = jdbcTemplate.queryForObject(queryMpaResult, this::mapRowToMpa, id);
        return resultMpa;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa((resultSet.getInt("RATING_MPA_ID")),
                (resultSet.getString("RATING_MPA_NAME")));
    }

    public boolean validateMpa(Integer id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM RATING WHERE RATING_MPA_ID = ?", id);
        return mpaRows.next();
    }

}
