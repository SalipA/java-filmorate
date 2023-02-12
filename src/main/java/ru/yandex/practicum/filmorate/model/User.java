package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.service.user.UserLoginConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NonNull
public class User {
    Set<Long> friends = new HashSet<>();

    @Email
    private String email;

    @UserLoginConstraint
    private String login;

    private String name;

    @Past
    private LocalDate birthday;
    private Long id;

    public void setFriends(Long userId) {
        friends.add(userId);
    }
}