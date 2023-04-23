package ru.yandex.practicum.filmorate.storage.friend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    private final Logger log = LoggerFactory.getLogger(FriendDbStorage.class);

    public FriendDbStorage(JdbcTemplate jdbcTemplate, @Lazy @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (userStorage.validateUser(userId) && userStorage.validateUser(friendId)) {
            jdbcTemplate.update("INSERT INTO FRIEND (FRIEND_CLIENT_ID, FRIEND_FRIEND_ID, FRIEND_STATUS) VALUES (?, ?, ?)", userId, friendId, true);
            log.info("Пользователь с id {} добавлен в друзья пользователю с id {}", friendId, userId);
        } else {
            throw new FriendNotFoundException(String.format("Пользователь с id %s или пользователь с id %s отсутствуют", userId, friendId));
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        if (userStorage.validateUser(userId) && userStorage.validateUser(friendId)) {
            jdbcTemplate.update("DELETE FROM FRIEND WHERE FRIEND_CLIENT_ID = ? AND FRIEND_FRIEND_ID = ?", userId, friendId);
            log.info("Пользователь с id {} удален из друзей пользователя с id {}", friendId, userId);
        } else {
            throw new FriendNotFoundException(String.format("Пользователь с id %s или пользователь с id %s отсутствуют", userId, friendId));
        }
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        if (userStorage.validateUser(userId)) {
            return jdbcTemplate.query("SELECT FRIEND.FRIEND_FRIEND_ID, CLIENT.* " +
                            "FROM FRIEND " +
                            "JOIN CLIENT ON FRIEND.FRIEND_FRIEND_ID = CLIENT.CLIENT_ID " +
                            "WHERE FRIEND.FRIEND_CLIENT_ID = ? AND FRIEND.FRIEND_STATUS = ?",
                    (rs, rowNum) -> new User(
                            rs.getLong("FRIEND.FRIEND_FRIEND_ID"),
                            rs.getString("EMAIL"),
                            rs.getString("LOGIN"),
                            rs.getString("CLIENT_NAME"),
                            rs.getDate("BIRTHDAY").toLocalDate()),
                    userId, true
            );
        } else {
            throw new FriendNotFoundException(String.format("Пользователь с id %s отсутствует", userId));
        }
    }

    @Override
    public Set<Long> getFriendsIds(Long userId) {
        return new HashSet<>(jdbcTemplate.query("SELECT FRIEND_FRIEND_ID, FRIEND_STATUS FROM FRIEND " +
                        "WHERE FRIEND_CLIENT_ID = ? AND FRIEND_STATUS = true", (rs, rowNum) -> (
                        rs.getLong("FRIEND_FRIEND_ID")),
                userId
        ));
    }
}

