package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface LikeStorage {
    void addLike(Long userId, Long filmId);

    void removeLike(Long filmId, Long userId);

    Collection<Film> getPopular(int count);

    Set<Long> getFilmsLikes(Long id);
}
