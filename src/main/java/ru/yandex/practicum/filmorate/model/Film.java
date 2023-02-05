package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.service.FilmReleaseDateConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NonNull
public class Film {
    private int id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @FilmReleaseDateConstraint
    private LocalDate releaseDate;

    @Positive
    private int duration;
}