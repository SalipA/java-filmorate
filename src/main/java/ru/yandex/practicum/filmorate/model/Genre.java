package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Genre {
    private Long id;
    @Size(max = 20)
    private String name;

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
