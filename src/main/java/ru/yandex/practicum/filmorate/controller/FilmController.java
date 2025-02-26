package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int startID = 0;
    private static final LocalDate RELEASE_DATE_VALIDATION = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getFilms() {
        log.info("GET, Films count: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        /*
         * Валидация фильма*/
        validateFilm(film);
        /*
         * Проверка, что у нас уже нет такого фильма*/
        if (films.containsValue(film)) {
            log.info("Film add error, film already exist, film: {}", film);
            throw new IllegalArgumentException("Film already exists");
        }
        /*
         * Добавление фильма*/
        film.setId(generateID());
        films.put(film.getId(), film);
        log.info("Film added: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        /*
         * Валидация фильма по заданным критериям*/
        validateFilm(film);
        /*
         * Проверка, что у нас уже нет такого фильма*/
        for (Film f : films.values()) {
            if (f.equals(film)) {
                return f;
            }
        }
        /*
         * Обновление фильма, если нам передали его id*/
        if (film.getId() != null) {
            if (films.containsKey(film.getId())) {
                films.replace(film.getId(), film);
                log.info("Film updated: {}", film);
                return film;
            }
            log.info("Film update failed, film: {}", film);
            throw new IllegalArgumentException("Film with this id does not exist");
        }
        /*
         * Добавление фильма*/
        film.setId(generateID());
        films.put(film.getId(), film);
        log.info("Film added: {}", film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName().isBlank() ||
                film.getDescription().isBlank() ||
                film.getDescription().length() > 200 ||
                film.getDuration() < 0 ||
                film.getReleaseDate().isBefore(RELEASE_DATE_VALIDATION)) {
            log.info("Film PUT validation failed, film: {}", film);
            throw new IllegalArgumentException("Film validation error ");
        }
    }

    private int generateID() {
        return startID++;
    }
}
