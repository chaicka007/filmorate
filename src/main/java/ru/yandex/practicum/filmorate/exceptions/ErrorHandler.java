package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

@RestControllerAdvice
public class ErrorHandler {
    private final ResourceBundle bundle = ResourceBundle.getBundle("exceptionLocale");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse ObjectNotFoundException(ObjectNotFoundException e) {
        return new ErrorResponse(String.format(bundle.getString("OBJECT_NOT_FOUND"),
                e.getObjectName(),
                LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse ObjectAlreadyExistException(ObjectAlreadyExistException e) {
        return new ErrorResponse(String.format(bundle.getString("OBJECT_ALREADY_EXIST"),
                e.getObjectName(),
                LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse BadRequestException(BadRequestException e) {
        return new ErrorResponse(String.format(bundle.getString("BAD_REQUEST"),
                e.getParameters(),
                LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unknownException(Throwable e) {
        return new ErrorResponse(e.getMessage());
    }
}
