package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilmToStorage(Film film);

    Film updateFilmInStorage(Film film);

    Film getFilmFromStorage(Long filmId);

    List<Film> getAll();
}
