package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addFriend(Long id, Long friendId) {
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(
                    String.format("При попытке добавить в друзья произошла ошибка: " +
                            "пользователь с id: %s не добавлен в базу", id));
        }
        if (!users.containsKey(friendId)) {
            throw new UserNotFoundException(
                    String.format("При попытке добавить в друзья произошла ошибка: " +
                            "пользователь с id: %s не добавлен в базу", id));
        }
        //Добавляю друга - friendId пользователю id
        User user1 = users.get(id);
        Set<Long> userFriends1 = user1.getFriends();
        userFriends1.add(friendId);

        //Добавляю друга - id пользователю friendId
        User user2 = users.get(friendId);
        Set<Long> userFriends2 = user2.getFriends();
        userFriends2.add(id);

        return String.format("Пользователь с id: %d добавлен в друзья пользователю с id: %s", friendId, id);
    }

    public String deleteFriend(Long id, Long friendId) {
        if (id < 0 || friendId < 0 || id == friendId) {
            throw new IllegalArgumentException("id пользователя и friendId не должны быть меньше 0 " +
                    "и равняться друг другу");
        }
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(
                    String.format("При попытке удалить из друзей произошла ошибка: " +
                            "пользователь с id: %s не добавлен в базу", id));
        }
        if (!users.containsKey(friendId)) {
            throw new UserNotFoundException(
                    String.format("При попытке удалить из друзей произошла ошибка: " +
                            "пользователь с id: %s не добавлен в базу", id));
        }
        //Удаляю друга - friendId у пользователя id
        User user1 = users.get(id);
        Set<Long> userFriends1 = user1.getFriends();
        userFriends1.remove(friendId);

        //Удаляю друга - id у пользователя friendId
        User user2 = users.get(friendId);
        Set<Long> userFriends2 = user2.getFriends();
        userFriends2.remove(id);

        return String.format("Пользователь с id: %d удален из друзей", friendId);
    }

    public Collection<User> getFriends(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException("id не должен быть меньше 0");
        }
        Collection<User> friends = new ArrayList<>();
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(
                    String.format("При попытке получить список друзей произошла ошибка: " +
                            "пользователь с id: %s не добавлен в базу", id));
        }
        User user = users.get(id);
        Set<Long> userFriends = user.getFriends();
        for (Long friendId : userFriends) {
            if (users.containsKey(friendId)) {
                friends.add(users.get(friendId));
            }
        }
        return friends;
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        if (id < 0 || otherId < 0 || id == otherId) {
            throw new IllegalArgumentException("id и otherId не должны быть меньше 0 и равняться друг другу");
        }
        Map<Long, User> users = userStorage.getUsers();
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(
                    String.format("При попытке получить список общих друзей произошла ошибка: " +
                            "пользователь с id: %s не добавлен в базу", id));
        }
        if (!users.containsKey(otherId)) {
            throw new UserNotFoundException(
                    String.format("При попытке получить список общих друзей произошла ошибка: " +
                            "пользователь с id: %s не добавлен в базу", id));
        }
        User user = users.get(id);
        Set<Long> userFriends = user.getFriends();

        User otherUser = users.get(otherId);
        Set<Long> otherUserFriends = otherUser.getFriends();

        Collection<User> commonFriends = userFriends.stream()
                .filter(x -> otherUserFriends.contains(x)).map(x -> users.get(x))
                .collect(Collectors.toCollection(ArrayList::new));
        return commonFriends;
    }
}
