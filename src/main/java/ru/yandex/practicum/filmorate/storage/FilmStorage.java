package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface FilmStorage {

    List<Film> getFilms();

    Film getFilm(Long id);

    Film addFilm(Film film);

    void addLike(Long filmId, Long LongID);

    void removeLike(Long filmId, Long userID);

    HashMap<Long, HashSet<Long>> getLikes();

    Set<Long> getLikesByFilmId(Long filmId);

    Integer getLikesCount(Long filmId);

    Film updateFilm(Film film);

    void deleteFilm(Long id);

    boolean contains(Film film);

    boolean contains(Long id);
}
