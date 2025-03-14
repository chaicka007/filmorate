package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    User getUser(Integer id);

    List<Integer> getFriends(Integer id);

    void addFriend(Integer id, Integer friendID);

    void removeFriend(Integer id, Integer friendID);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);

    boolean contains(User user);

    boolean contains(Integer id);

}
