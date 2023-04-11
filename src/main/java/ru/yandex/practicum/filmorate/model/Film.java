package ru.yandex.practicum.filmorate.model;

import java.lang.annotation.*;
import java.time.LocalDate;

import lombok.Data;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.*;

@Data
public class Film {
    private int id;

    @NotEmpty(message = "Название фильма не может быть пустым")
    @NotNull(message = "Название фильма не может быть пустым")
    private String name;

    @NotBlank(message = "Описание фильма не может быть пустым")
    @Size(max = 200, message = "Описание фильма не может превышать 200 символов")
    private String description;

    @NotNull(message = "Дата выходы фильма не может быть пустой")
    @After("1895-12-28")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма не может быть пустой")
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    private long duration;

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
