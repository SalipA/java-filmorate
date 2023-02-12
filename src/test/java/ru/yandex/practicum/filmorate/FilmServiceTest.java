package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilmServiceTest {
    FilmService FS;
    Film STANDARD_CASE_FILM;
    Film STANDARD_CASE_FILM_2;

    @BeforeEach
    public void createFilmService() {
        FS = new FilmService(new InMemoryFilmStorage());
    }

    @BeforeEach
    public void createStandardFilms() {
        STANDARD_CASE_FILM = new Film();
        STANDARD_CASE_FILM.setName("testFilm");
        STANDARD_CASE_FILM.setDescription("testDescription");
        STANDARD_CASE_FILM.setDescription("testDescription");
        STANDARD_CASE_FILM.setReleaseDate(LocalDate.of(2023, 2, 23));
        STANDARD_CASE_FILM.setDuration(60);

        STANDARD_CASE_FILM_2 = new Film();
        STANDARD_CASE_FILM_2.setName("testFilm2");
        STANDARD_CASE_FILM_2.setDescription("testDescription2");
        STANDARD_CASE_FILM_2.setDescription("testDescription2");
        STANDARD_CASE_FILM_2.setReleaseDate(LocalDate.of(2023, 3, 23));
        STANDARD_CASE_FILM_2.setDuration(100);
    }

    @Test
    public void shouldAddFilmInStorageStandardCase() throws Exception {
        Film testFilm = FS.createFilm(STANDARD_CASE_FILM);
        Assertions.assertEquals(1L, testFilm.getId());
        Assertions.assertEquals(FS.getFilmById(1L), testFilm);
        Assertions.assertEquals(1, FS.getAllFilms().size());
    }

    @Test
    public void shouldAddFilmInStorageIfFilmAlreadyExist() throws Exception {
        FS.createFilm(STANDARD_CASE_FILM);
        final AlreadyExistException exp = Assertions.assertThrows(AlreadyExistException.class,
            () -> FS.createFilm(STANDARD_CASE_FILM)
        );
        Assertions.assertEquals("Фильм с названием <testFilm> уже был добавлен. Id = 1", exp.getMessage());
    }

    @Test
    public void shouldUpdateFilmInStorageStandardCase() throws Exception {
        Film testFilm = FS.createFilm(STANDARD_CASE_FILM);
        testFilm.setDescription("Update Desc");
        FS.updateFilm(testFilm);
        Assertions.assertEquals("Update Desc", testFilm.getDescription());
        Assertions.assertEquals(FS.getFilmById(1L), testFilm);
        Assertions.assertEquals(1, FS.getAllFilms().size());
    }

    @Test
    public void shouldUpdateFilmInStorageIfFilmIdNull() {
        final NotFoundException exp = Assertions.assertThrows(NotFoundException.class,
            () -> FS.updateFilm(STANDARD_CASE_FILM)
        );
        Assertions.assertEquals("Фильм с id = null не найден", exp.getMessage());
    }

    @Test
    public void shouldUpdateFilmInStorageIfFilmNotFound() throws Exception {
        Film testFilm = FS.createFilm(STANDARD_CASE_FILM);
        testFilm.setDescription("Update Desc");
        testFilm.setId(3L);
        final NotFoundException exp = Assertions.assertThrows(NotFoundException.class,
            () -> FS.updateFilm(testFilm)
        );
        Assertions.assertEquals("Фильм с id = 3 не найден", exp.getMessage());
    }

    @Test
    public void shouldGetAllFilmsFromStorageStandardCase() throws Exception {
        Film testFilm1 = FS.createFilm(STANDARD_CASE_FILM);
        Film testFilm2 = FS.createFilm(STANDARD_CASE_FILM_2);
        List<Film> testList = List.of(testFilm1, testFilm2);
        Assertions.assertEquals(2, FS.getAllFilms().size());
        Assertions.assertEquals(testList, FS.getAllFilms());
    }

    @Test
    public void shouldGetFilmByIdStandardCase() throws Exception {
        FS.createFilm(STANDARD_CASE_FILM);
        STANDARD_CASE_FILM.setId(1L);
        Assertions.assertEquals(STANDARD_CASE_FILM, FS.getFilmById(1L));
    }

    @Test
    public void shouldGetFilmByIdIfIdNotFound() {
        final NotFoundException exp = Assertions.assertThrows(NotFoundException.class,
            () -> FS.getFilmById(3L)
        );
        Assertions.assertEquals("Фильм с id = 3 не найден", exp.getMessage());
    }

    @Test
    public void shouldAddLikeToFilmStandardCase() throws Exception {
        FS.createFilm(STANDARD_CASE_FILM);
        FS.addLike(1L, 101L);
        STANDARD_CASE_FILM.setLikes(101L);
        Assertions.assertEquals(STANDARD_CASE_FILM, FS.getFilmById(1L));
        Assertions.assertEquals(1, FS.getFilmById(1L).getLikes().size());
        Assertions.assertTrue(FS.getFilmById(1L).getLikes().contains(101L));
    }

    @Test
    public void shouldAddLikeToFilmMoreThanOneTime() throws Exception {
        FS.createFilm(STANDARD_CASE_FILM);
        FS.addLike(1L, 101L);
        FS.addLike(1L, 101L);
        Assertions.assertEquals(STANDARD_CASE_FILM, FS.getFilmById(1L));
        Assertions.assertEquals(1, FS.getFilmById(1L).getLikes().size());
        Assertions.assertTrue(FS.getFilmById(1L).getLikes().contains(101L));
    }

    @Test
    public void shouldAddLikeToFilmIfFilmIdNotFound() {
        final NotFoundException exp = Assertions.assertThrows(NotFoundException.class,
            () -> FS.addLike(2L, 101L)
        );
        Assertions.assertEquals("Фильм с id = 2 не найден", exp.getMessage());
    }

    @Test
    public void shouldRemoveLikeFromFilmStandardCase() throws Exception {
        FS.createFilm(STANDARD_CASE_FILM);
        FS.addLike(1L, 101L);
        FS.addLike(1L, 102L);
        Assertions.assertEquals(2, FS.getFilmById(1L).getLikes().size());
        FS.removeLike(1L, 102L);
        Assertions.assertEquals(1, FS.getFilmById(1L).getLikes().size());
        Assertions.assertTrue(FS.getFilmById(1L).getLikes().contains(101L));
    }

    @Test
    public void shouldRemoveLikeFromFilmIfFilmNotFound() {
        final NotFoundException exp = Assertions.assertThrows(NotFoundException.class,
            () -> FS.removeLike(2L, 101L)
        );
        Assertions.assertEquals("Фильм с id = 2 не найден", exp.getMessage());
    }

    @Test
    public void shouldRemoveLikeFromFilmIfUserIdNotFound() throws Exception {
        FS.createFilm(STANDARD_CASE_FILM);
        FS.addLike(1L, 101L);
        final NotFoundException exp = Assertions.assertThrows(NotFoundException.class,
            () -> FS.removeLike(1L, 102L)
        );
        Assertions.assertEquals("Лайк пользователя с id = 102 невозможно удалить", exp.getMessage());
    }

    @Test
    public void shouldGetPopularFilmsStandardCase() throws Exception {
        FS.createFilm(STANDARD_CASE_FILM);
        FS.createFilm(STANDARD_CASE_FILM_2);
        FS.addLike(1L, 101L);
        Assertions.assertEquals(2, FS.getPopular(2).size());
        Assertions.assertEquals(FS.getFilmById(1L), FS.getPopular(2).get(0));
    }

    @Test
    public void shouldGetPopularFilmsIfCount10() throws Exception {
        List<Film> tenFilms = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tenFilms.add(new Film());
        }
        for (int i = 0; i < 10; i++) {
            tenFilms.get(i).setName(Integer.toString(i));
            tenFilms.get(i).setReleaseDate(LocalDate.of(2023, 1, i + 1));
            tenFilms.get(i).setDuration(i + 10);
        }
        for (Film film : tenFilms) {
            FS.createFilm(film);
        }
        FS.addLike(2L, 1L);
        FS.addLike(2L, 2L);
        FS.addLike(3L, 2L);
        FS.addLike(4L, 2L);
        FS.addLike(4L, 1L);
        FS.addLike(4L, 3L);
        FS.addLike(4L, 4L);
        FS.addLike(5L, 1L);
        FS.addLike(6L, 1L);
        FS.addLike(6L, 2L);
        FS.addLike(6L, 3L);
        FS.addLike(7L, 2L);
        FS.addLike(7L, 3L);
        FS.addLike(9L, 2L);
        FS.addLike(9L, 3L);
        FS.addLike(10L, 3L);

        Assertions.assertEquals(10, FS.getPopular(10).size());
        Assertions.assertEquals(FS.getFilmById(4L), FS.getPopular(10).get(0));
    }

    @Test
    public void shouldGetPopularFilmsIfCount11() throws Exception {
        List<Film> tenFilms = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            tenFilms.add(new Film());
        }
        for (int i = 0; i < 11; i++) {
            tenFilms.get(i).setName(Integer.toString(i));
            tenFilms.get(i).setReleaseDate(LocalDate.of(2023, 1, i + 1));
            tenFilms.get(i).setDuration(i + 10);
        }
        for (Film film : tenFilms) {
            FS.createFilm(film);
        }
        FS.addLike(2L, 1L);
        FS.addLike(2L, 2L);
        FS.addLike(3L, 2L);
        FS.addLike(4L, 2L);
        FS.addLike(4L, 1L);
        FS.addLike(4L, 3L);
        FS.addLike(4L, 4L);
        FS.addLike(5L, 1L);
        FS.addLike(6L, 1L);
        FS.addLike(6L, 2L);
        FS.addLike(6L, 3L);
        FS.addLike(7L, 2L);
        FS.addLike(7L, 3L);
        FS.addLike(9L, 2L);
        FS.addLike(9L, 3L);
        FS.addLike(10L, 3L);
        FS.addLike(11L, 3L);

        Assertions.assertEquals(10, FS.getPopular(10).size());
        Assertions.assertEquals(FS.getFilmById(4L), FS.getPopular(10).get(0));
    }

    @Test
    public void shouldGetPopularFilmsIfNotLikes() throws Exception {
        FS.createFilm(STANDARD_CASE_FILM);
        FS.createFilm(STANDARD_CASE_FILM_2);

        Assertions.assertEquals(2, FS.getPopular(2).size());
    }

    @Test
    public void shouldGetPopularIfRemoveLike() throws Exception {
        FS.createFilm(STANDARD_CASE_FILM);
        FS.createFilm(STANDARD_CASE_FILM_2);
        FS.addLike(1L, 1L);
        Assertions.assertEquals(2, FS.getPopular(20).size());
        Assertions.assertEquals(FS.getFilmById(1L), FS.getPopular(20).get(0));
        FS.addLike(2L, 2L);
        FS.addLike(2L, 3L);
        Assertions.assertEquals(2, FS.getPopular(20).size());
        Assertions.assertEquals(FS.getFilmById(2L), FS.getPopular(20).get(0));
        FS.removeLike(2L, 2L);
        FS.removeLike(2L, 3L);
        Assertions.assertEquals(2, FS.getPopular(20).size());
        Assertions.assertEquals(FS.getFilmById(1L), FS.getPopular(20).get(0));
    }
}