package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest {
    @Autowired
    FilmService filmService;
    @Autowired
    LikeStorage likeStorage;
    @Autowired
    FriendStorage friendStorage;
    @Autowired
    UserService userService;

    @Autowired
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;


    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void addFriendTest() {
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

        userService.addFriend(1L, 2L);
        assertEquals(1, userService.getFriends(1L).size());

        assertThrows(FriendNotFoundException.class, () -> userService.addFriend(1L, -2L));

    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void deleteFriendTest() {
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

        userService.addFriend(1L, 2L);

        userService.deleteFriend(1L, 2L);

        assertEquals(0, userService.getFriends(1L).size());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getFriendsTest() {
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

        User user3 = new User();
        user3.setEmail("Update@domain.com");
        user3.setName("Update");
        user3.setLogin("Update");
        user3.setBirthday(LocalDate.of(1990, 5, 4));

        userStorage.postUser(user);
        userStorage.postUser(user2);
        userStorage.postUser(user3);

        userService.addFriend(1L, 2L);
        userService.addFriend(1L, 3L);

        assertEquals(2, userService.getFriends(1L).size());
    }

    @Test
    @Sql({"/schema.sql", "/data.sql"})
    void getCommonFriendsTest() {
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

        User user3 = new User();
        user3.setEmail("Update@domain.com");
        user3.setName("Update");
        user3.setLogin("Update");
        user3.setBirthday(LocalDate.of(1990, 5, 4));

        userStorage.postUser(user);
        userStorage.postUser(user2);
        userStorage.postUser(user3);

        userService.addFriend(1L, 2L);
        userService.addFriend(3L, 2L);

        assertEquals(1, userService.getCommonFriends(1L, 3L).size());
    }
}
