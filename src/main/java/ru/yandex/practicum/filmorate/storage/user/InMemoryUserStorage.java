package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> idUsers = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public User addUserToStorage(User user) throws AlreadyExistException {
        if (idUsers.containsValue(user)) {
            throw new AlreadyExistException(user);
        } else {
            idCounter++;
            user.setId(idCounter);
            idUsers.put(idCounter, user);
            return user;
        }
    }

    @Override
    public User updateUserInStorage(User user) throws NotFoundException {
        if (idUsers.containsKey(user.getId())) {
            idUsers.remove(user.getId());
            idUsers.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
    }

    @Override
    public User getUserFromStorage(Long id) throws NotFoundException {
        if (idUsers.containsKey(id)) {
            return idUsers.get(id);
        } else {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(idUsers.values());
    }
}