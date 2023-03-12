package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.List;

@Component
public class RatingDaoImpl implements RatingDao {
    private final JdbcTemplate jdbcTemplate;

    public RatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating findRatingById(Long id) {
        String sqlFindRatingById = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sqlFindRatingById, id);
        if (ratingRows.next()) {
            return new Rating(ratingRows.getLong("RATING_ID"),
                ratingRows.getString("NAME"),
                ratingRows.getString("DESCRIPTION"));
        } else {
            throw new NotFoundException("Рейтинг MPA с id = " + id + " не найден");
        }
    }

    @Override
    public List<Rating> getAll() {
        List<Rating> allRatings = new ArrayList<>();
        String sqlGetAllRatings = "SELECT * FROM RATINGS";
        SqlRowSet ratingRowSet = jdbcTemplate.queryForRowSet(sqlGetAllRatings);
        while (ratingRowSet.next()) {
            allRatings.add(makeRating(ratingRowSet));
        }
        return allRatings;
    }

    private Rating makeRating(SqlRowSet rs) {
        Long id = rs.getLong("RATING_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        return new Rating(id, name, description);
    }
}
