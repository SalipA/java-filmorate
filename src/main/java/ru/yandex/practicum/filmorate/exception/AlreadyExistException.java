package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public class AlreadyExistException extends RuntimeException {

    public AlreadyExistException(Film film) {
        super("Фильм с названием <" + film.getName() + "> уже был добавлен. Id = " + film.getId());
    }

    public AlreadyExistException(User user) {
        super("Пользователь с email <" + user.getEmail() + "> уже был добавлен. Id = " + user.getId());
    }
}