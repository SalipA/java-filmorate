package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.SQLException;
import java.util.List;

public interface RatingDao {
    Rating findRatingById(Long id) throws NotFoundException;

    List<Rating> getAll() throws SQLException;
}
