package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenre() {
        String sqlQueryMpa = "SELECT * FROM GENRE;";
        return jdbcTemplate.query(sqlQueryMpa, this::mapRowToGenre);
    }

    @Override
    public Genre getGenre(int id) {
        if (!validateGenre(id)) {
            throw new GenreNotFoundException(String.format("При получении жанра обнаружена ошибка: жанр с id %d отсутствует", id));
        }
        String queryGenreResult = "SELECT * FROM GENRE WHERE GENRE_ID = ?;";

        Genre genreResult = jdbcTemplate.queryForObject(queryGenreResult, this::mapRowToGenre, id);
        return genreResult;
    }

    @Override
    public void removeFilmsGenres(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_CATEGORY WHERE FILM_CATEGORY_FILM_ID = ?", film.getId());
    }

    @Override
    public void addFilmsGenres(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(
                        "INSERT INTO FILM_CATEGORY (FILM_CATEGORY_FILM_ID, FILM_CATEGORY_GENRE_ID) VALUES (?, ?)", film.getId(), genre.getId()
                );
            }
        }
    }

    @Override
    public Set<Genre> getFilmsGenres(Long id) {
        return new HashSet<>(jdbcTemplate.query("SELECT GENRE.GENRE_ID, GENRE.GENRE_NAME FROM FILM_CATEGORY" +
                        " JOIN GENRE ON FILM_CATEGORY.FILM_CATEGORY_GENRE_ID = GENRE.GENRE_ID" +
                        " WHERE FILM_CATEGORY.FILM_CATEGORY_FILM_ID = ?" +
                        " ORDER BY GENRE.GENRE_ID ", (rs, rowNum) -> new Genre(
                        rs.getInt("GENRE.GENRE_ID"),
                        rs.getString("GENRE.GENRE_NAME")),
                id
        ));
    }


    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre((resultSet.getInt("GENRE_ID")),
                (resultSet.getString("GENRE_NAME")));
    }

    public boolean validateGenre(Integer id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE GENRE_ID = ?", id);
        return genreRows.next();
    }
}
