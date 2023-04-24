package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User {
    long id;
    Set<Long> friends = new HashSet<>();
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(regexp = "^[_A-Za-z0-9+-]+(?:[.'’][_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(?:\\.[_A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$",
            message = "Некорректно указан Email")
    String email;
    @NotBlank(message = "Логин не может быть пустой")
    @Pattern(regexp = "^[a-zA-Z]+\\w+$", message = "Логин не может содержать пробелы")
    String login;
    String name;
    @Past(message = "Дата рождения не может быть в будущем")
    @NotNull(message = "Электронная почта не может быть пустой")
    LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(String login, String name, long id, String email, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(String login, Set<Long> friends, String name, long id, String email, LocalDate birthday) {
        this.id = id;
        this.friends = friends;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> param = new HashMap<>();
        param.put("EMAIL", email);
        param.put("LOGIN", login);
        param.put("CLIENT_NAME", name);
        param.put("BIRTHDAY", birthday);
        return param;
    }
}

