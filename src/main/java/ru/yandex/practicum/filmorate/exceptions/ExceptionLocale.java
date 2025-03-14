package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;

@Getter
public enum ExceptionLocale {
    FILM_ALREADY_EXIST_EXCEPTION("Film %s already exist"),
    FILM_NOT_EXIST_EXCEPTION("Film with ID %d not exists"),
    USER_ALREADY_EXIST_EXCEPTION("User with %s already exist"),
    USER_NOT_EXIST_EXCEPTION("User with id %s not found"),
    USER_AND_FRIEND_IDS_EQUALS_EXCEPTION("User and friend IDs is equals"),
    INCORRECT_PARAMETER_EXCEPTION("Not valid parameter %s"),
    VALIDATION_EXCEPTION("Parameter %s not valid");

    private final String locale;

    ExceptionLocale(String locale) {
        this.locale = locale;
    }


    @Override
    public String toString() {
        return getLocale();
    }
}
