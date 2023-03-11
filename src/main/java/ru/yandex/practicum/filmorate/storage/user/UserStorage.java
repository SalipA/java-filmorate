package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUserToStorage(User user) throws AlreadyExistException;

    User updateUserInStorage(User user) throws NotFoundException;

    User getUserFromStorage(Long id) throws NotFoundException;

    List<User> getAll();
}
