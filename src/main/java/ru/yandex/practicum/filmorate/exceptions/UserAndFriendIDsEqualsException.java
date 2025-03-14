package ru.yandex.practicum.filmorate.exceptions;

public class UserAndFriendIDsEqualsException extends RuntimeException {
    public UserAndFriendIDsEqualsException(String message) {
        super(message);
    }
}
