package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RatingDaoImpl implements RatingDao {
    private final JdbcTemplate jdbcTemplate;

    public RatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating findRatingById(Long id) throws NotFoundException {
        String sqlQuery = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (ratingRows.next()) {
            return new Rating(ratingRows.getLong("RATING_ID"),
                ratingRows.getString("NAME"),
                ratingRows.getString("DESCRIPTION"));
        } else {
            throw new NotFoundException("Рейтинг MPA с id = " + id + " не найден");
        }
    }

    @Override
    public List<Rating> getAll() throws SQLException {
        String sqlQuery = "SELECT * FROM RATINGS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeRating(rs));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        Long id = rs.getLong("RATING_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        return new Rating(id, name, description);
    }
}
