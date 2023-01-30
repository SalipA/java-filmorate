package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends Controller<Film> {
    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate MIN_RELEASE_DAT = LocalDate.of(1895, 12, 28);

    @Override
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        try {
            validateInput(film);
            counter++;
            film.setId(counter);
            allValues.put(film.getId(), film);
            return film;
        } catch (ValidationException exp) {
            throw new ValidationException(exp.getMessage());
        }
    }

    @Override
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        try {
            validateInput(film);
            if (allValues.containsKey(film.getId())) {
                allValues.remove(film.getId());
                allValues.put(film.getId(), film);
                return film;
            } else {
                throw new ValidationException("Фильм с данным id не найден");
            }
        } catch (ValidationException exp) {
            throw new ValidationException(exp.getMessage());
        }
    }

    private void validateInput(Film film) throws ValidationException {
        if (allValues.containsValue(film)) {
            throw new ValidationException("Данный фильм уже был добавлен");
        } else if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription() != null && film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            throw new ValidationException("Длина описания больше максимальной возможной");
        } else if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MIN_RELEASE_DAT)) {
            throw new ValidationException("Дата релиза ранее самой ранней возможной");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}