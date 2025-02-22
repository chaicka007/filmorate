package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    private int startID = 0;

    @GetMapping
    public Map<Integer, User> getUsers() {
        return new HashMap<>(users);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        /*
         * Проверка, что ДР не в будущем */
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User validation failed, user: {}", user);
            throw new ValidationException("birthday is after now");
        }
        /*
        Обновление пользователя */
        if (users.containsValue(user)) {
            log.warn("User add failed, email already exist, user: {}", user);
            throw new ValidationException("User with this email already exists");
        }
        /*
         * Добавление пользователя*/
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateID());
        users.put(user.getId(), user);
        log.info("User added: {}", user.getEmail());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        /*
         * Проверка, что ДР не в будущем */
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User validation failed, user: {}", user);
            throw new ValidationException("birthday is after now");
        }
        /*
        Обновление пользователя */
        if (user.getId() != null) {
            if (users.containsKey(user.getId())) {
                users.replace(user.getId(), user);
                log.info("User updated: {}", user.getEmail());
                return user;
            }
            log.warn("User update failed, user: {}", user);
            throw new ValidationException("User with this id does not exist");
        }
        /*
         * Добавление пользователя*/
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateID());
        users.put(user.getId(), user);
        log.info("User added: {}", user.getEmail());
        return user;
    }

    private int generateID() {
        return startID++;
    }
}
