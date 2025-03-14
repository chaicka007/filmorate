package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ExceptionLocale;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserAndFriendIDsEqualsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private int startID = 0;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        /*
         * Валидация пользователя по заданным критериям  */
        validateUser(user);
        /*
        Обновление пользователя */
        if (userStorage.contains(user)) {
            log.info("User add failed, email already exist, user: {}", user);
            throw new UserAlreadyExistException(String.format(ExceptionLocale.USER_ALREADY_EXIST_EXCEPTION.toString(),
                    user.getEmail()));
        }
        /*
         * Добавление пользователя*/
        user.setId(generateID());
        userStorage.addUser(user);
        log.info("User added: {}", user.getEmail());
        return user;
    }

    public User updateUser(User user) {
        /*
         * Валидация пользователя по заданным критериям  */
        validateUser(user);
        /*
        Обновление пользователя */
        if (user.getId() != null) {
            userStorage.updateUser(user);
            log.info("User updated: {}", user.getEmail());
            return user;
        }
        /*
         * Добавление пользователя*/
        return addUser(user);
    }

    public List<User> getFriends(Integer id) {
        if (!userStorage.contains(id)){
            throw new UserNotExistException(String.format(ExceptionLocale.USER_NOT_EXIST_EXCEPTION.toString()
                    ,id));
        }
        return userStorage.getUsers().stream()
                .filter(user -> userStorage.getFriends(id).contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId){
        return getFriends(id).stream()
                .filter(friend -> getFriends(otherId).contains(friend))
                .collect(Collectors.toList());
    }

    public List<User> addFriend(Integer userID, Integer friendID) {
        validateFriend(userID, friendID);
        userStorage.addFriend(userID, friendID);
        return getFriends(userID);
    }

    public List<User> removeFriend(Integer userID, Integer friendID) {
        validateFriend(userID, friendID);
        userStorage.removeFriend(userID, friendID);
        return getFriends(userID);
    }

    private void validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("User validation failed, user: {}", user);
            throw new ValidationException("birthday");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("login");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void validateFriend(Integer userID, Integer friendID) {
        if (userID.equals(friendID)) {
            throw new UserAndFriendIDsEqualsException(ExceptionLocale.USER_AND_FRIEND_IDS_EQUALS_EXCEPTION.toString());
        }
        if (!userStorage.contains(userID)) {
            throw new UserNotExistException(String.format(ExceptionLocale.USER_NOT_EXIST_EXCEPTION.toString(),
                    userID));
        }
        if (!userStorage.contains(friendID)) {
            throw new UserNotExistException(String.format(ExceptionLocale.USER_NOT_EXIST_EXCEPTION.toString(),
                    friendID));
        }
    }

    private int generateID() {
        return startID++;
    }
}
