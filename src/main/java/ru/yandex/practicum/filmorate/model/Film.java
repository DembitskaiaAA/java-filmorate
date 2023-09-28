package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.*;
import java.lang.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Film {
    long id;
    Set<Long> likes = new HashSet<>();

    @NotEmpty(message = "Название фильма не может быть пустым")
    @NotNull(message = "Название фильма не может быть пустым")
    String name;

    @NotBlank(message = "Описание фильма не может быть пустым")
    @Size(max = 200, message = "Описание фильма не может превышать 200 символов")
    String description;

    Mpa mpa;

    @NotNull(message = "Дата выходы фильма не может быть пустой")
    @After("1895-12-28")
    LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма не может быть пустой")
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    long duration;

    Set<Genre> genres = new HashSet<>();

    int rate;

    public Film(String name, LocalDate releaseDate, String description, long duration, int rate, Mpa mpa,
                Set<Genre> genres) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.description = description;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;

    }

    public Film(String name, LocalDate releaseDate, String description, long duration, int rate, Mpa mpa) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.description = description;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(Long id, String name, LocalDate releaseDate, String description, long duration, int rate,
                Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.description = description;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(long id, Set<Long> likes, String name, LocalDate releaseDate, String description,
                long duration, int rate, Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.likes = likes;
        this.name = name;
        this.description = description;
        this.mpa = mpa;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
        this.rate = rate;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> param = new HashMap<>();
        param.put("FILM_NAME", name);
        param.put("DESCRIPTION", description);
        param.put("RELEASE_DATE", releaseDate);
        param.put("DURATION", duration);
        param.put("RATE", rate);
        return param;
    }

    @Constraint(validatedBy = AfterValidator.class)
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface After {
        String message() default "Дата релиза фильма — не раньше 28 декабря 1895 года";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        String value();
    }

    public static class AfterValidator implements ConstraintValidator<After, LocalDate> {

        private LocalDate date;

        public void initialize(After annotation) {
            date = LocalDate.parse(annotation.value());
        }

        @Override
        public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
            boolean valid = true;
            if (value != null) {
                if (!value.isAfter(date)) {
                    valid = false;
                }
            }
            return valid;
        }
    }
}
