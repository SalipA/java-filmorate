package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilmToStorage(Film film) throws AlreadyExistException;

    Film updateFilmInStorage(Film film) throws NotFoundException;

    Film getFilmFromStorage(Long filmId) throws NotFoundException;

    List<Film> getAll();
}
