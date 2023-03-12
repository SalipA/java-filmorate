package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final int POPULAR_FROM_MAX_TO_MIN_SORT_RATE = -1;
    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate MIN_RELEASE_DAT = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    Comparator<Film> compPopular = Comparator.comparingInt(o -> POPULAR_FROM_MAX_TO_MIN_SORT_RATE * o.getLikes().size());

    @Autowired
    public FilmService(@Qualifier("DbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        validateInput(film);
        return filmStorage.addFilmToStorage(film);
    }

    public Film updateFilm(Film film) {
        validateInput(film);
        return filmStorage.updateFilmInStorage(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmFromStorage(filmId);
    }

    public Film addLike(Long filmId, Long userId) {
        Film foundFilm = filmStorage.getFilmFromStorage(filmId);
        foundFilm.setLikes(userId);
        filmStorage.updateFilmInStorage(foundFilm);
        return foundFilm;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film foundFilm = filmStorage.getFilmFromStorage(filmId);
        if (foundFilm.getLikes().contains(userId)) {
            foundFilm.getLikes().remove(userId);
            filmStorage.updateFilmInStorage(foundFilm);
            return foundFilm;
        } else {
            throw new NotFoundException("Лайк пользователя с id = " + userId + " невозможно удалить");
        }
    }

    public List<Film> getPopular(Integer count) {

        List<Film> sortedList = filmStorage.getAll().stream()
            .sorted(compPopular)
            .collect(Collectors.toList());

        if (sortedList.size() <= count) {
            return sortedList;
        } else {
            return sortedList.subList(0, count);
        }
    }

    private void validateInput(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
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