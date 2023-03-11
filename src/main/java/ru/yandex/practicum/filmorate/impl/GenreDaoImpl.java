package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findGenreById(Long id) throws NotFoundException {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (genreRows.next()) {
            return new Genre(genreRows.getLong("GENRE_ID"),
                genreRows.getString("NAME"));
        } else {
            throw new NotFoundException("Жанр с id = " + id + " не найден");
        }
    }

    @Override
    public List<Genre> getAll() throws SQLException {
        String sql = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre makeGenre(ResultSet rs) throws SQLException {
        Long id = rs.getLong("GENRE_ID");
        String name = rs.getString("NAME");
        return new Genre(id, name);
    }

}
