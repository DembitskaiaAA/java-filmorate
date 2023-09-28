package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmDbStorageTest {

    @Autowired
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void testGetAllFilms() {
        // Создаем несколько фильмов для теста, используя конструкторы Mpa и Genre.
        Film film1 = new Film("Title 1", LocalDate.of(2022, 1, 1),
                "Description 1", 120, 7, new Mpa(1, "G"),
                Set.of(new Genre(1, "Комедия")));
        Film film2 = new Film("Title 2", LocalDate.of(2022, 2, 2),
                "Description 2", 90, 6, new Mpa(2, "PG-13"),
                Set.of(new Genre(2, "Драма")));
        Film film3 = new Film("Title 3", LocalDate.of(2022, 3, 3),
                "Description 3", 150, 8, new Mpa(1, "G"),
                Set.of(new Genre(2, "Драма")));

        filmStorage.postFilm(film1);
        filmStorage.postFilm(film2);
        filmStorage.postFilm(film3);

        // Получаем все фильмы из базы данных.
        Collection<Film> films = filmStorage.getAllFilms();

        // Проверяем, что список всех фильмов не пустой и содержит созданные фильмы.
        assertFalse(films.isEmpty());
        assertThat(films).usingElementComparatorIgnoringFields("likes").contains(film1);
        assertThat(films).usingElementComparatorIgnoringFields("likes").contains(film2);
        assertThat(films).usingElementComparatorIgnoringFields("likes").contains(film3);

        // Удаляем созданные фильмы из базы данных.
        filmStorage.deleteFilm(film1.getId());
        filmStorage.deleteFilm(film2.getId());
        filmStorage.deleteFilm(film3.getId());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void testPostFilm() {
        // Создаем новый фильм для теста.
        Film film = new Film("Title", LocalDate.of(2022, 1, 1),
                "Description", 120, 7, new Mpa(2, "PG-13"),
                Set.of(new Genre(2, "Драма")));

        // Сохраняем фильм в базе данных с помощью метода postFilm().
        Film savedFilm = filmStorage.postFilm(film);

        // Проверяем, что фильм был успешно сохранен в базе данных и имеет уникальный id.
        assertNotNull(savedFilm.getId());
        assertEquals(film.getName(), savedFilm.getName());
        assertEquals(film.getReleaseDate(), savedFilm.getReleaseDate());
        assertEquals(film.getDescription(), savedFilm.getDescription());
        assertEquals(film.getDuration(), savedFilm.getDuration());
        assertEquals(film.getMpa(), savedFilm.getMpa());
        assertEquals(film.getGenres(), savedFilm.getGenres());

        // Удаляем созданный фильм из базы данных.
        filmStorage.deleteFilm(savedFilm.getId());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void testUpdateFilm() {
        // Создаем тестовый исходный фильм
        Film srcFilm = new Film("test film", LocalDate.of(2022, 4, 23),
                "description", 120, 8, new Mpa(2, "PG-13"),
                Set.of(new Genre(2, "Драма")));
        // Создаем тестовый обновленный фильм
        Film updatedFilm = new Film(1L, "updated test film", LocalDate.of(2022, 4, 23),
                "updated description", 130, 9, new Mpa(1, "G"),
                Set.of(new Genre(2, "Драма")));

        filmStorage.postFilm(srcFilm);

        // Вызываем метод updateFilm() с обновленным фильмом
        Film result = filmStorage.updateFilm(updatedFilm);

        // Проверяем, что метод вернул обновленный фильм
        assertEquals(updatedFilm.getId(), result.getId());
        assertEquals(updatedFilm.getName(), result.getName());
        assertEquals(updatedFilm.getDescription(), result.getDescription());
        assertEquals(updatedFilm.getReleaseDate(), result.getReleaseDate());
        assertEquals(updatedFilm.getDuration(), result.getDuration());
        assertEquals(updatedFilm.getRate(), result.getRate());
        assertEquals(updatedFilm.getMpa(), result.getMpa());
        assertEquals(updatedFilm.getGenres(), result.getGenres());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void testDeleteFilm() {
        // Создаем тестовый исходный фильм
        Film srcFilm = new Film("test film", LocalDate.of(2022, 4, 23),
                "description", 120, 8, new Mpa(2, "PG-13"),
                Set.of(new Genre(2, "Драма")));

        filmStorage.postFilm(srcFilm);

        Collection<Film> films = filmStorage.getAllFilms();

        assertEquals(films.size(), 1);

        filmStorage.deleteFilm(srcFilm.getId());
        Collection<Film> filmResult = filmStorage.getAllFilms();
        assertEquals(filmResult.size(), 0);
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void testGetFilm() {
        Long id = 1L;
        // Создаем тестовый фильм в базе данных
        Film srcFilm = new Film("test film", LocalDate.of(2022, 4, 23),
                "description", 120, 8, new Mpa(2, "PG"),
                Set.of(new Genre(2, "Драма")));

        filmStorage.postFilm(srcFilm);

        // Вызываем метод getFilm()
        Film result = filmStorage.getFilm(srcFilm.getId());

        // Проверяем, что метод вернул ожидаемый фильм
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("test film", result.getName());
        assertEquals("description", result.getDescription());
        assertEquals(LocalDate.of(2022, 4, 23), result.getReleaseDate());
        assertEquals(120, result.getDuration());
        assertEquals(8, result.getRate());
        assertEquals(new Mpa(2, "PG"), result.getMpa());
        assertEquals(Set.of(new Genre(2, "Драма")), result.getGenres());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    public void testGetFilmWrongId() {
        Long id = -5L;
        // Создаем тестовый фильм в базе данных
        Film srcFilm = new Film("test film", LocalDate.of(2022, 4, 23),
                "description", 120, 8, new Mpa(2, "PG"),
                Set.of(new Genre(2, "Драма")));

        filmStorage.postFilm(srcFilm);
        assertThrows(FilmNotFoundException.class, () -> filmStorage.getFilm(id));
    }

}