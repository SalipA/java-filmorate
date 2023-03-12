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
    Set<User> friends = new HashSet<>();

    @Email
    private String email;

    @UserLoginConstraint
    private String login;

    private String name;

    @Past
    private LocalDate birthday;
    private Long id;

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User() {
    }

    public void setFriends(User user) {
        friends.add(user);
    }

    public void setFriendsSet(Set<User> friendsSet) {
        this.friends = friendsSet;
    }
}