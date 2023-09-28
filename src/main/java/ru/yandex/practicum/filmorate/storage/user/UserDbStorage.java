package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FriendStorage friendStorage;


    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendStorage friendStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendStorage = friendStorage;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQueryUsers = "SELECT * FROM CLIENT;";
        return jdbcTemplate.query(sqlQueryUsers, this::mapRowToUser);
    }

    @Override
    public User postUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("CLIENT")
                .usingGeneratedKeyColumns("CLIENT_ID");
        user.setId(simpleJdbcInsertUser.executeAndReturnKey(user.toMap()).longValue());

        String queryUserResult = "SELECT * FROM CLIENT WHERE CLIENT_ID = ?;";

        User userResult = jdbcTemplate.queryForObject(queryUserResult, this::mapRowToUser, user.getId());
        return userResult;
    }

    @Override
    public User updateUser(User user) {
        if (!validateUser(user.getId())) {
            throw new UserNotFoundException(
                    String.format("При обновлении пользователя обнаружена ошибка: пользователя с id %d отсутствует",
                            user.getId()));
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(
                "UPDATE CLIENT SET EMAIL = ?, LOGIN = ?, CLIENT_NAME = ?," +
                        " BIRTHDAY = ? WHERE CLIENT_ID = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public String deleteUser(Long id) {
        if (!validateUser(id)) {
            throw new UserNotFoundException(
                    String.format("При удалении пользователя обнаружена ошибка: пользователь с id %d отсутствует", id));
        }
        String sqlQueryDeleteUser = "DELETE from CLIENT where CLIENT_ID = ?";
        jdbcTemplate.update(sqlQueryDeleteUser, id);
        return String.format("пользователь с id: %s удален", id);
    }

    @Override
    public User getUser(Long id) {
        if (!validateUser(id)) {
            throw new UserNotFoundException(
                    String.format("При получении пользователя обнаружена ошибка: пользователя с id %d отсутствует",
                            id));
        }
        String queryUserResult = "SELECT * FROM CLIENT WHERE CLIENT_ID = ?;";

        User userResult = jdbcTemplate.queryForObject(queryUserResult, this::mapRowToUser, id);
        return userResult;
    }

    @Override
    public Map<Long, User> getUsers() {
        return null;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User((resultSet.getString("LOGIN")),
                friendStorage.getFriendsIds(resultSet.getLong("CLIENT_ID")),
                (resultSet.getString("CLIENT_NAME")),
                (resultSet.getLong("CLIENT_ID")),
                (resultSet.getString("EMAIL")),
                ((resultSet.getDate("BIRTHDAY")).toLocalDate()));
    }

    @Override
    public boolean validateUser(Long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM CLIENT WHERE CLIENT_ID = ?", userId);
        return userRows.next();
    }
}
