package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private long startID = 0;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        if (userStorage.contains(user)) {
            log.debug("User add failed, email already exist, user: {}", user);
            throw new ObjectAlreadyExistException("User");
        }
        validateUser(user);
        user.setId(generateID());
        userStorage.addUser(user);
        log.debug("User added: {}", user.getEmail());
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == null) {
            return addUser(user);
        }
        validateUser(user);
        userStorage.updateUser(user);
        log.debug("User updated: {}", user.getEmail());
        return user;
    }

    public List<User> getFriends(Long id) {
        if (!userStorage.contains(id)) {
            throw new ObjectNotFoundException("User");
        }
        return userStorage.getUsers().stream()
                .filter(user -> userStorage.getFriends(id).contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        return getFriends(id).stream()
                .filter(friend -> getFriends(otherId).contains(friend))
                .collect(Collectors.toList());
    }

    public List<User> addFriend(Long userID, Long friendID) {
        validateFriend(userID, friendID);
        userStorage.addFriend(userID, friendID);
        return getFriends(userID);
    }

    public void removeFriend(Long userID, Long friendID) {
        validateFriend(userID, friendID);
        userStorage.removeFriend(userID, friendID);
    }

    private void validateUser(User user) {
        ArrayList<String> errorParameters = new ArrayList<>();
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("User validation failed, user: {}", user);
            errorParameters.add("birthday");
        }
        if (user.getLogin().contains(" ")) {
            errorParameters.add("login");
        }
        if (!errorParameters.isEmpty()) {
            throw new BadRequestException(errorParameters);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void validateFriend(Long userID, Long friendID) {
        if (userID.equals(friendID)) {
            throw new BadRequestException(new ArrayList<>(List.of("friendId", "userID")));
        }
        if (!userStorage.contains(userID) || !userStorage.contains(friendID)) {
            throw new ObjectNotFoundException("User");
        }
    }

    private long generateID() {
        return startID++;
    }
}
