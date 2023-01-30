package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NonNull
public class User {
    private int id;

    @Email
    private String email;
    @NotBlank
    private String login;

    private String name;
    private LocalDate birthday;
}
