package ru.yandex.practicum.filmorate.service;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FilmReleaseDateConstraintValidator.class)
public @interface FilmReleaseDateConstraint {
    String message() default "{FilmReleaseDate.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}