package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUserToStorage(User user);

    User updateUserInStorage(User user);

    User getUserFromStorage(Long id);

    List<User> getAll();
}
