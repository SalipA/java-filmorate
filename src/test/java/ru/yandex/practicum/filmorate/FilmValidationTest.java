package ru.yandex.practicum.filmorate;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

public class FilmValidationTest {
    static FilmController FC;
    static Validator validator;
    Film testFilm;

    @BeforeAll
    public static void setupValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeAll
    public static void createControllers() {
        FC = new FilmController();
    }

    @BeforeEach
    public void createTestFilm() {
        testFilm = new Film();
    }

    @Test
    public void shouldThrowExpIfCreateFilmsNameIsEmpty() {
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> FC.create(testFilm)
        );
        Assertions.assertEquals("Название не может быть пустым", exp.getMessage());
    }

    @Test
    public void shouldThrowExpIfCreateFilmDescriptionLengthIsMoreThen200Chars() {
        String testDescription = RandomString.make(205);
        testFilm.setName("testName");
        testFilm.setDescription(testDescription);
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> FC.create(testFilm)
        );
        Assertions.assertEquals("Длина описания больше максимальной возможной", exp.getMessage());
    }

    @Test
    public void shouldThrowExpIfCreateFilmReleaseDateIsBeforeMinDate() {
        testFilm.setName("testName");
        testFilm.setReleaseDate(LocalDate.of(1600, 12, 12));
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> FC.create(testFilm)
        );
        Assertions.assertEquals("Дата релиза ранее самой ранней возможной", exp.getMessage());
    }

    @Test
    public void shouldThrowExpIfCreateFilmDurationIsMinus() {
        testFilm.setName("testName");
        testFilm.setDuration(-10);
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> FC.create(testFilm)
        );
        Assertions.assertEquals("Продолжительность фильма должна быть положительной", exp.getMessage());
    }

    @Test
    public void shouldThrowExpIfCreateFilmsNameIsBlank() {
        testFilm.setName("");
        System.out.println(testFilm.getName().isBlank());
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> FC.create(testFilm)
        );
        Assertions.assertEquals("Название не может быть пустым", exp.getMessage());
    }

    @Test
    public void annotationNotBlankTestForFilmName() {
        testFilm.setName(" ");
        testFilm.setDuration(10);
        testFilm.setReleaseDate(LocalDate.of(2000, 12, 12));
        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    public void annotationSizeMax200TestForFilmDescriptionLengthIsMoreThen200Chars() {
        String testDescription = RandomString.make(205);
        testFilm.setName("testName");
        testFilm.setDuration(10);
        testFilm.setReleaseDate(LocalDate.of(2000, 12, 12));
        testFilm.setDescription(testDescription);
        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    public void annotationFilmReleaseDateConstraintTestForFilmReleaseDateNull() {
        testFilm.setName("testName");
        testFilm.setDuration(10);
        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    public void annotationFilmReleaseDateConstraintTestForFilmReleaseDateIsBeforeMinDate() {
        testFilm.setName("testName");
        testFilm.setDuration(10);
        testFilm.setReleaseDate(LocalDate.of(1600, 12, 12));
        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    public void annotationFilmReleaseDateConstraintTestForFilmReleaseDateIs28_12_1895() {
        testFilm.setName("testName");
        testFilm.setDuration(10);
        testFilm.setReleaseDate(LocalDate.of(1895, 12, 28));
        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    public void annotationFilmReleaseDateConstraintTestForFilmReleaseDateIsAfterMinDate() {
        testFilm.setName("testName");
        testFilm.setDuration(10);
        testFilm.setReleaseDate(LocalDate.of(2000, 12, 28));
        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    public void annotationPositiveFilmDurationTestForFilmIfDurationIsNegative() {
        testFilm.setName("testName");
        testFilm.setDuration(-10);
        testFilm.setReleaseDate(LocalDate.of(2000, 12, 12));
        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        Assertions.assertEquals(1, violations.size());
    }
}