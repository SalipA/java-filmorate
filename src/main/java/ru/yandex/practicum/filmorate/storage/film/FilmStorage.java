package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.List;

public interface FilmStorage {
    Film addFilmToStorage(Film film) throws AlreadyExistException, NotFoundException;

    Film updateFilmInStorage(Film film) throws NotFoundException, AlreadyExistException;

    Film getFilmFromStorage(Long filmId) throws NotFoundException, SQLException;

    List<Film> getAll() throws NotFoundException, SQLException;
}
