package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface FilmStorage {

    List<Film> getFilms();

    Film getFilm(Integer id);

    Film addFilm(Film film);

    void addLike(Integer filmId, Integer userID);

    void removeLike(Integer filmId, Integer userID);

    HashMap<Integer, HashSet<Integer>> getLikes();

    Set<Integer> getLikesByFilmId(Integer filmId);

    Integer getLikesCount(Integer filmId);

    Film updateFilm(Film film);

    void deleteFilm(Integer id);

    boolean contains(Film film);

    boolean contains(Integer id);
}
