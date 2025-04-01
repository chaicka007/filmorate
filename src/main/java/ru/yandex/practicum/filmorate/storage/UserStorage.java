package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    User getUser(Long id);

    List<Long> getFriends(Long id);

    void addFriend(Long id, Long friendID);

    void removeFriend(Long id, Long friendID);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    boolean contains(User user);

    boolean contains(Long id);

}
