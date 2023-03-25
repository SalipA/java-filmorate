package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

@RestController
@RequestMapping(value = "/mpa", produces = "application/json;charset=UTF-8")
public class RatingController {
    private final RatingDao ratingDao;

    public RatingController(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    @GetMapping("/{id}")
    public Rating get(@PathVariable Long id) {
        return ratingDao.findRatingById(id);
    }

    @GetMapping
    public List<Rating> getAlL() {
        return ratingDao.getAll();
    }
}
