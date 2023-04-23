package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public String addFriend(Long id, Long friendId) {
        friendStorage.addFriend(id, friendId);
        return String.format("Пользователь с id: %d добавлен в друзья пользователю с id: %s", friendId, id);
    }

    public String deleteFriend(Long id, Long friendId) {
        friendStorage.removeFriend(id, friendId);
        return String.format("Пользователь с id: %d удален из друзей", friendId);
    }

    public Collection<User> getFriends(Long id) {
        return friendStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return friendStorage.getFriends(id).stream()
                .filter(x -> friendStorage.getFriends(otherId).contains(x))
                .collect(Collectors.toList());
    }
}
