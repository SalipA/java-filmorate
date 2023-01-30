package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NonNull
public class Film {
    private int id;

    @NotBlank
    private String name;

    private String description;
    private LocalDate releaseDate;
    private int duration;
    
}