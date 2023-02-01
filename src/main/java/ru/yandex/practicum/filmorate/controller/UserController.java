package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends Controller<User> {
    @Override
    public User create(@Valid @RequestBody User user) throws ValidationException {
            validateInput(user);
            counter++;
            user.setId(counter);
            allValues.put(user.getId(), user);
            return user;
    }

    @Override
    public User update(@Valid @RequestBody User user) throws ValidationException {
        validateInput(user);
        if (allValues.containsKey(user.getId())) {
            allValues.remove(user.getId());
            allValues.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Пользователь с данным id не найден");
        }
    }

    private void validateInput(User user) throws ValidationException {
        if (allValues.containsValue(user)) {
            throw new ValidationException("Данный пользователь уже был добавлен");
        } else if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() != null && user.getLogin().indexOf(" ") > 0 || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}