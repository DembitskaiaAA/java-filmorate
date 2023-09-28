package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserDbStorageTest {
    @Autowired
    FilmService filmService;
    @Autowired
    LikeStorage likeStorage;
    @Autowired
    FriendStorage friendStorage;
    @Autowired
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;

    @Autowired
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;


    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void postUserTest() {
        User user = new User();
        user.setEmail("newmail@domain.com");
        user.setName("Alex Johnson");
        user.setLogin("alexj");
        user.setBirthday(LocalDate.of(1995, 4, 15));

        User user2 = new User();
        user2.setEmail("anothermail@domain.com");
        user2.setName("Anna Smith");
        user2.setLogin("annasmith88");
        user2.setBirthday(LocalDate.of(1988, 7, 22));

        userStorage.postUser(user);
        userStorage.postUser(user2);
        assertEquals(2, userStorage.getAllUsers().size());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void updateUserTest() {
        User user = new User();
        user.setEmail("fourthmail@domain.com");
        user.setName("Jane Johnson");
        user.setLogin("janejohnson456");
        user.setBirthday(LocalDate.of(1990, 5, 4));

        User user2 = new User();
        user2.setEmail("Update@domain.com");
        user2.setName("Update");
        user2.setLogin("Update");
        user2.setBirthday(LocalDate.of(1990, 5, 4));
        user2.setId(1L);

        userStorage.postUser(user);
        userStorage.updateUser(user2);

        assertEquals(user2, userStorage.getUser(1L));
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getAllUsersTest() {
        User user = new User();
        user.setEmail("newmail@domain.com");
        user.setName("Alex Johnson");
        user.setLogin("alexj");
        user.setBirthday(LocalDate.of(1995, 4, 15));

        User user2 = new User();
        user2.setEmail("anothermail@domain.com");
        user2.setName("Anna Smith");
        user2.setLogin("annasmith88");
        user2.setBirthday(LocalDate.of(1988, 7, 22));

        userStorage.postUser(user);
        userStorage.postUser(user2);

        assertEquals(2, userStorage.getAllUsers().size());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void deleteUserTest() {
        User user = new User();
        user.setEmail("newmail@domain.com");
        user.setName("Alex Johnson");
        user.setLogin("alexj");
        user.setBirthday(LocalDate.of(1995, 4, 15));

        User user2 = new User();
        user2.setEmail("anothermail@domain.com");
        user2.setName("Anna Smith");
        user2.setLogin("annasmith88");
        user2.setBirthday(LocalDate.of(1988, 7, 22));

        userStorage.postUser(user);
        userStorage.postUser(user2);

        userStorage.deleteUser(2L);

        assertEquals(1, userStorage.getAllUsers().size());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getUserTest() {
        User user = new User();
        user.setEmail("newmail@domain.com");
        user.setName("Alex Johnson");
        user.setLogin("alexj");
        user.setBirthday(LocalDate.of(1995, 4, 15));

        user = userStorage.postUser(user);

        assertEquals(user, userStorage.getUser(1L));
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getUserWrongIdTest() {
        User user = new User();
        user.setEmail("newmail@domain.com");
        user.setName("Alex Johnson");
        user.setLogin("alexj");
        user.setBirthday(LocalDate.of(1995, 4, 15));

        assertThrows(UserNotFoundException.class, () -> userStorage.getUser(-1L));
    }

}


