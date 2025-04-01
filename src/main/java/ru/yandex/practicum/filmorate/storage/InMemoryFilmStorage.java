package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, HashSet<Long>> likesPerFilm = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Long id) {
        return films.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        likesPerFilm.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userID) {
        likesPerFilm.get(filmId).add(userID);
    }

    @Override
    public void removeLike(Long filmId, Long userID) {
        likesPerFilm.get(filmId).remove(userID);
    }

    @Override
    public Set<Long> getLikesByFilmId(Long filmId) {
        return new HashSet<>(likesPerFilm.get(filmId));
    }

    @Override
    public HashMap<Long, HashSet<Long>> getLikes() {
        return new HashMap<>(likesPerFilm);
    }

    @Override
    public Integer getLikesCount(Long filmId) {
        return likesPerFilm.get(filmId).size();
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ObjectNotFoundException("Film");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(Long id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Film");
        }
        likesPerFilm.remove(id);
        films.remove(id);
    }

    @Override
    public boolean contains(Film film) {
        return films.containsValue(film);
    }

    @Override
    public boolean contains(Long id) {
        return films.containsKey(id);
    }
}
