package ru.yandex.practicum.filmorate.service.film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmReleaseDateConstraintValidator implements ConstraintValidator<FilmReleaseDateConstraint, LocalDate> {
    private static final LocalDate MIN_RELEASE_DAT = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        if (releaseDate == null) {
            return false;
        } else return releaseDate.isEqual(MIN_RELEASE_DAT) || releaseDate.isAfter(MIN_RELEASE_DAT);
    }
}