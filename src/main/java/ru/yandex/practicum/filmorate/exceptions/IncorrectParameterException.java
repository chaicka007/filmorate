package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;

@Getter
public class IncorrectParameterException extends RuntimeException {
    private final String parameter;

    public IncorrectParameterException(String parameter) {
        super();
        this.parameter = parameter;
    }

}
