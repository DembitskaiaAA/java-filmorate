package ru.yandex.practicum.filmorate.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Component
public class GenreController {

    GenreStorage genreStorage;


    public GenreController(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @GetMapping
    public Collection<Genre> getAllGenre() {
        return genreStorage.getAllGenre();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        return genreStorage.getGenre(id);
    }

}
