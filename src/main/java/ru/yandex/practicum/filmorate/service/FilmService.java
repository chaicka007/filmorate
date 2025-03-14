package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ExceptionLocale;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.exceptions.UserNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exceptions.ExceptionLocale.FILM_ALREADY_EXIST_EXCEPTION;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserService userService;
    private int startID = 0;
    private static final LocalDate RELEASE_DATE_VALIDATION = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.userService = userService;
    }

    public List<Film> getFilms() {
        log.info("GET, Films count: {}", filmStorage.getFilms().size());
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        /*
         * Валидация фильма*/
        validateFilm(film);
        /*
         * Проверка, что у нас уже нет такого фильма*/
        if (filmStorage.contains(film)) {
            log.info("Film add error, film already exist, film: {}", film);
            throw new FilmAlreadyExistException(String.format(FILM_ALREADY_EXIST_EXCEPTION.toString(), film.getName()));
        }
        /*
         * Добавление фильма*/
        film.setId(generateID());
        filmStorage.addFilm(film);
        log.info("Film added: {}", film);
        return film;
    }

    public Film getFilm(int id) {
        if (!filmStorage.contains(id)) {
            throw new FilmNotExistException(String.format(ExceptionLocale.FILM_NOT_EXIST_EXCEPTION.toString(),
                    id));
        }
        return filmStorage.getFilm(id);
    }

    public Film updateFilm(Film film) {
        /*
         * Валидация фильма по заданным критериям*/
        validateFilm(film);
        /*
         * Проверка, что у нас уже нет такого фильма*/
        if (filmStorage.contains(film)) {
            return film;
        }
        /*
         * Обновление фильма, если нам передали его id*/
        if (film.getId() != null) {
            filmStorage.updateFilm(film);
            log.info("Film updated: {}", film);
            return film;
        }
        /*
         * Добавление фильма*/
        return addFilm(film);
    }

    public List<User> addLike(Integer filmId, Integer userId) {
        validateLike(filmId, userId);
        filmStorage.addLike(filmId, userId);
        return getLikes(filmId);
    }

    public List<User> removeLike(Integer filmId, Integer userId) {
        validateLike(filmId, userId);
        filmStorage.removeLike(filmId, userId);
        return getLikes(filmId);
    }

    public List<User> getLikes(Integer filmId) {
        return userService.getUsers().stream()
                .filter(user -> filmStorage.getLikesByFilmId(filmId).contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<Film> getPopular(Integer size) {
        return filmStorage.getFilms().stream()
                .sorted((f0, f1) ->
                        filmStorage.getLikesCount(f0.getId()).compareTo(filmStorage.getLikesCount(f1.getId())))
                .limit(size)
                .collect(Collectors.toList());
    }

    private void validateFilm(Film film) {
        if (film.getName().isBlank()) {
            log.info("Film PUT validation failed, film: {}", film);
            throw new ValidationException("name");
        }
        if (film.getDescription().isBlank() ||
                film.getDescription().length() > 200) {
            log.info("Film PUT validation failed, film: {}", film);
            throw new ValidationException("description");
        }
        if (film.getDuration() < 0) {
            log.info("Film PUT validation failed, film: {}", film);
            throw new ValidationException("duration");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE_VALIDATION)) {
            log.info("Film PUT validation failed, film: {}", film);
            throw new ValidationException("releaseDate");
        }
    }

    private void validateLike(Integer filmId, Integer userId) {
        if (!filmStorage.contains(filmId)) {
            throw new FilmNotExistException(String.format(ExceptionLocale.FILM_NOT_EXIST_EXCEPTION.toString(),
                    filmId));
        }
        if (!userStorage.contains(userId)) {
            throw new UserNotExistException(String.format(ExceptionLocale.USER_NOT_EXIST_EXCEPTION.toString(),
                    userId));
        }
    }

    private int generateID() {
        return startID++;
    }
}
