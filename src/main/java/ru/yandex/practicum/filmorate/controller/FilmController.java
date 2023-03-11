package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/films", produces = "application/json;charset=UTF-8")
@Slf4j
public class FilmController extends Controller<Film> {
    private final static String DEFAULT_VALUE_POPULAR_LIST_SIZE = "10";
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    public Film create(@Valid @RequestBody Film film) throws ValidationException, AlreadyExistException,
        NotFoundException {
        return filmService.createFilm(film);
    }

    @Override
    public Film update(@Valid @RequestBody Film film) throws ValidationException, NotFoundException,
        AlreadyExistException {
        return filmService.updateFilm(film);
    }

    @Override
    public List<Film> getAll() throws NotFoundException, SQLException {
        return filmService.getAllFilms();
    }

    @Override
    @GetMapping("/{id}")
    public Film get(@PathVariable Long id) throws NotFoundException, SQLException {
        return filmService.getFilmById(id);
    }

    @Override
    @PutMapping("/{id}/like/{userId}")
    public Film put(@PathVariable Long id, @PathVariable Long userId) throws NotFoundException, SQLException,
        AlreadyExistException {
        return filmService.addLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = DEFAULT_VALUE_POPULAR_LIST_SIZE, required = false)
                                     Integer count) throws NotFoundException, SQLException {
        return filmService.getPopular(count);
    }

    @Override
    @DeleteMapping("/{id}/like/{userId}")
    public Film delete(@PathVariable Long id, @PathVariable Long userId) throws NotFoundException, SQLException,
        AlreadyExistException {
        return filmService.removeLike(id, userId);
    }
}