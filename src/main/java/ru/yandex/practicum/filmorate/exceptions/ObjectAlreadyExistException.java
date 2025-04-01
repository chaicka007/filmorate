package ru.yandex.practicum.filmorate.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectAlreadyExistException extends RuntimeException {
    private final String objectName;
}
