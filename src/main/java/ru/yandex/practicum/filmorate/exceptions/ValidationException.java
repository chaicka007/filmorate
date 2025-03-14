package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final String parameter;

    public ValidationException(String parameter) {
        super();
        this.parameter = parameter;
    }
}
