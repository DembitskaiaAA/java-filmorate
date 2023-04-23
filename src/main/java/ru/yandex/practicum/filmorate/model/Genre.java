package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Genre {
    @Valid
    @Positive
    @Max(value = 11, message = "id жанра фильма не может быть больше 11")
    private int id;
    private String name;

        public Genre(int id, String name) {
            this.id = id;
            this.name = name;
        }

    public Map<String, Object> toMap() {
        Map<String, Object> param = new HashMap<>();
        param.put("GENRE_ID", id);
        param.put("GENRE_NAME", name);
        return param;
    }
}
