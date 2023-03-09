package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class User {
    private int id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(regexp = "^[_A-Za-z0-9+-]+(?:[.'’][_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(?:\\.[_A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$",
            message = "Некорректно указан Email")
    private String email;
    @NotBlank(message = "Логин не может быть пустой")
    @Pattern(regexp = "^[a-zA-Z]+\\w+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    @NotNull(message = "Электронная почта не может быть пустой")
    private LocalDate birthday;
}

