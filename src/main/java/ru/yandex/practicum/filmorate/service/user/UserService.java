package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("DbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        validateInput(user);
        return userStorage.addUserToStorage(user);
    }

    public User update(User user) {
        validateInput(user);
        return userStorage.updateUserInStorage(user);
    }

    public User getUserById(Long userId) {
        return userStorage.getUserFromStorage(userId);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User addFriend(Long UserId, Long FriendId) {
        User user = userStorage.getUserFromStorage(UserId);
        User usersFriend = userStorage.getUserFromStorage(FriendId);
        user.setFriends(usersFriend);
        userStorage.updateUserInStorage(user);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUserFromStorage(userId);
        User usersFriend = userStorage.getUserFromStorage(friendId);
        if (user.getFriends().contains(usersFriend)) {
            user.getFriends().remove(usersFriend);
            userStorage.updateUserInStorage(user);
            return user;
        } else {
            throw new NotFoundException("Удалить из друзей пользователя с id = " + usersFriend.getId() + " невозможно");
        }
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUserFromStorage(userId);
        Set<User> userFriends = user.getFriends();
        return new LinkedList<>(userFriends);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUserFromStorage(userId);
        User usersFriend = userStorage.getUserFromStorage(friendId);
        Set<User> userFriendsId = user.getFriends();
        Set<User> usersFriendFriendsId = usersFriend.getFriends();
        Set<User> common = new HashSet<>(userFriendsId);
        common.retainAll(usersFriendFriendsId);
        List<User> commonFriendsList = new ArrayList<>();
        for (User commonUser : common) {
            commonFriendsList.add(userStorage.getUserFromStorage(commonUser.getId()));
        }
        return commonFriendsList;
    }

    private void validateInput(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
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