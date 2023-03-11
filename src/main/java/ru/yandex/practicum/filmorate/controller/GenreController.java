package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/genres", produces = "application/json;charset=UTF-8")
public class GenreController {
    private final GenreDao genreDao;

    public GenreController(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable Long id) throws NotFoundException {
        return genreDao.findGenreById(id);
    }

    @GetMapping
    public List<Genre> getAlL() throws SQLException {
        return genreDao.getAll();
    }
}
