package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findGenreById(Long id) {
        String sqlFindGenreById = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlFindGenreById, id);
        if (genreRows.next()) {
            return new Genre(genreRows.getLong("GENRE_ID"),
                genreRows.getString("NAME"));
        } else {
            throw new NotFoundException("Жанр с id = " + id + " не найден");
        }
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> allGenres = new ArrayList<>();
        String sqlGetAllGenres = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        SqlRowSet genreRowSet = jdbcTemplate.queryForRowSet(sqlGetAllGenres);
        while (genreRowSet.next()) {
            allGenres.add(makeGenre(genreRowSet));
        }
        return allGenres;
    }

    private Genre makeGenre(SqlRowSet rs) {
        Long id = rs.getLong("GENRE_ID");
        String name = rs.getString("NAME");
        return new Genre(id, name);
    }
}
