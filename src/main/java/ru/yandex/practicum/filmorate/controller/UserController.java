package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends Controller<User> {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User create(@Valid @RequestBody User user) throws ValidationException, AlreadyExistException {
        return userService.create(user);
    }

    @Override
    public User update(@Valid @RequestBody User user) throws ValidationException, NotFoundException {
        return userService.update(user);
    }

    @Override
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) throws NotFoundException {
        return userService.getUserById(id);
    }

    @Override
    @PutMapping("/{id}/friends/{friendId}")
    public User put(@PathVariable Long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.addFriend(id, friendId);
    }

    @Override
    @DeleteMapping("/{id}/friends/{friendId}")
    public User delete(@PathVariable Long id, @PathVariable Long friendId) throws NotFoundException {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) throws NotFoundException {
        return userService.getFriends(id);
    }


    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(@PathVariable Long id, @PathVariable Long otherId) throws NotFoundException {
        return userService.getCommonFriends(id, otherId);
    }
}