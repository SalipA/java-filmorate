package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.service.UserLoginConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NonNull
public class User {
    private int id;

    @Email
    private String email;

    @UserLoginConstraint
    private String login;

    private String name;

    @Past
    private LocalDate birthday;
}
