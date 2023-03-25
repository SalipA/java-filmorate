package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Rating {
    private Long id;
    @Size(max = 5)
    private String name;
    @Size(max = 50)
    private String description;

    public Rating(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
