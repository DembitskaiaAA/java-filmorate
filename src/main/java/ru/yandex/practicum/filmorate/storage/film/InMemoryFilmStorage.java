package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();

    private long id = 0;

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film postFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен в список", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Обнаружена ошибка: фильм с id {} отсутствует", film.getId());
            throw new FilmNotFoundException(String.format("Обнаружена ошибка: фильм с id %d отсутствует", film.getId()));
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен в списке", film.getName());
        return film;
    }

    @Override
    public String deleteFilm(Long id) {
        if (!films.containsKey(id)) {
            log.error("Обнаружена ошибка: фильм с id {} отсутствует", id);
            throw new FilmNotFoundException(String.format("Обнаружена ошибка: фильм с id %d отсутствует", id));
        }
        films.remove(id);
        return String.format("Фильм с id: %s удален", id);
    }

    @Override
    public Film getFilm(Long id) {
        if (!films.containsKey(id)) {
            log.error("Обнаружена ошибка: фильм с id {} отсутствует", id);
            throw new FilmNotFoundException(String.format("Обнаружена ошибка: фильм с id %d отсутствует", id));
        }
        return films.get(id);
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public boolean validateFilm(Long filmId) {
        return false;
    }
}
