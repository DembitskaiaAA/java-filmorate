package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface FriendStorage {
    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    Collection<User> getFriends(Long userId);

    Set<Long> getFriendsIds(Long userId);
}
