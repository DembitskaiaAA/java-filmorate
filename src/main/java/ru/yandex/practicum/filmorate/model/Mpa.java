package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    @Valid
    @Positive
    @Max(value = 5, message = "id рейтинга фильма не может быть больше 5")
    private int id;
    private String name;


    public Map<String, Object> toMap() {
        Map<String, Object> param = new HashMap<>();
        param.put("RATING_MPA_ID", id);
        param.put("RATING_MPA_NAME", name);
        return param;
    }

}
