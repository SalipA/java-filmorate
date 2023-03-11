package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface GenreDao {
    Genre findGenreById(Long id) throws NotFoundException;

    List<Genre> getAll() throws SQLException;

    Genre makeGenre(ResultSet rs) throws SQLException;
}
