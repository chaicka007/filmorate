package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ExceptionLocale;
import ru.yandex.practicum.filmorate.exceptions.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, HashSet<Integer>> likesPerFilm = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Integer id) {
        return films.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        likesPerFilm.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public void addLike(Integer filmId, Integer userID) {
        likesPerFilm.get(filmId).add(userID);
    }

    @Override
    public void removeLike(Integer filmId, Integer userID) {
        likesPerFilm.get(filmId).remove(userID);
    }

    @Override
    public Set<Integer> getLikesByFilmId(Integer filmId) {
        return new HashSet<>(likesPerFilm.get(filmId));
    }

    @Override
    public HashMap<Integer, HashSet<Integer>> getLikes() {
        return new HashMap<>(likesPerFilm);
    }

    @Override
    public Integer getLikesCount(Integer filmId) {
        return likesPerFilm.get(filmId).size();
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException(String.format(ExceptionLocale.FILM_NOT_EXIST_EXCEPTION.toString(),
                    film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmNotExistException(String.format(ExceptionLocale.FILM_NOT_EXIST_EXCEPTION.toString(),
                    id));
        }
        likesPerFilm.remove(id);
        films.remove(id);
    }

    @Override
    public boolean contains(Film film) {
        return films.containsValue(film);
    }

    @Override
    public boolean contains(Integer id) {
        return films.containsKey(id);
    }
}
