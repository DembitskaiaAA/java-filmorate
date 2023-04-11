package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) throws ValidationException {
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен в список", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.error("Обнаружена ошибка: фильм с id {} отсутствует", film.getId());
            throw new ValidationException("Обнаружена ошибка: фильм с id " + film.getId() + " отсутствует");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен в списке", film.getName());
        return film;
    }
}
