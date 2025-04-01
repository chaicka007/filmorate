package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserService userService;
    private long startID = 0;
    private static final LocalDate RELEASE_DATE_VALIDATION = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.userService = userService;
    }

    public List<Film> getFilms() {
        log.debug("GET, Films count: {}", filmStorage.getFilms().size());
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        if (filmStorage.contains(film)) {
            log.debug("Film add error, film already exist, film: {}", film);
            throw new ObjectAlreadyExistException("Film");
        }
        film.setId(generateID());
        filmStorage.addFilm(film);
        log.debug("Film added: {}", film);
        return film;
    }

    public Film getFilm(long id) {
        if (!filmStorage.contains(id)) {
            throw new ObjectNotFoundException("Film");
        }
        return filmStorage.getFilm(id);
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            return addFilm(film);
        }
        if (!filmStorage.contains(film.getId())) {
            throw new ObjectNotFoundException("Film");
        }

        validateFilm(film);
        filmStorage.updateFilm(film);
        log.debug("Film updated: {}", film);
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        validateLike(filmId, userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        validateLike(filmId, userId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<User> getLikes(Long filmId) {
        return userService.getUsers().stream()
                .filter(user -> filmStorage.getLikesByFilmId(filmId).contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<Film> getPopular(Integer size) {
        if (size == null){
            throw new BadRequestException(new ArrayList<>(List.of("size")));
        }
        return filmStorage.getFilms().stream()
                .sorted((f0, f1) ->
                        filmStorage.getLikesCount(f0.getId()).compareTo(filmStorage.getLikesCount(f1.getId())))
                .limit(size)
                .collect(Collectors.toList());
    }

    private void validateFilm(Film film) {
        ArrayList<String> errorParameters = new ArrayList<>();
        if (film.getName().isBlank()) {
            log.debug("Film PUT validation failed, film: {}", film);
            errorParameters.add("name");
        }
        if (film.getDescription().isBlank() ||
                film.getDescription().length() > 200) {
            log.debug("Film PUT validation failed, film: {}", film);
            errorParameters.add("description");
        }
        if (film.getDuration() < 0) {
            log.debug("Film PUT validation failed, film: {}", film);
            errorParameters.add("duration");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE_VALIDATION)) {
            log.debug("Film PUT validation failed, film: {}", film);
            errorParameters.add("releaseDate");
        }

        if (!errorParameters.isEmpty()) {
            throw new BadRequestException(errorParameters);
        }
    }

    private void validateLike(Long filmId, Long userId) {
        if (!filmStorage.contains(filmId)) {
            throw new ObjectNotFoundException("film");
        }
        if (!userStorage.contains(userId)) {
            throw new ObjectNotFoundException("user");
        }
    }

    private long generateID() {
        return startID++;
    }
}
