package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> idFilms = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public Film addFilmToStorage(Film film) {
        if (idFilms.containsValue(film)) {
            throw new AlreadyExistException(film);
        } else {
            idCounter++;
            film.setId(idCounter);
            idFilms.put(idCounter, film);
            return film;
        }
    }

    @Override
    public Film updateFilmInStorage(Film film) {
        if (idFilms.containsKey(film.getId())) {
            idFilms.remove(film.getId());
            idFilms.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
    }

    @Override
    public Film getFilmFromStorage(Long filmId) {
        if (idFilms.containsKey(filmId)) {
            return idFilms.get(filmId);
        } else {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(idFilms.values());
    }
}