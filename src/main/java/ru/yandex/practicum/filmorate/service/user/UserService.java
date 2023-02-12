package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) throws ValidationException, AlreadyExistException {
        validateInput(user);
        return userStorage.addUserToStorage(user);
    }

    public User update(User user) throws ValidationException, NotFoundException {
        validateInput(user);
        return userStorage.updateUserInStorage(user);
    }

    public User getUserById(Long userId) throws NotFoundException {
        return userStorage.getUserFromStorage(userId);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User addFriend(Long UserId, Long FriendId) throws NotFoundException {
        User user = userStorage.getUserFromStorage(UserId);
        User usersFriend = userStorage.getUserFromStorage(FriendId);
        user.setFriends(usersFriend.getId());
        usersFriend.setFriends(user.getId());
        userStorage.updateUserInStorage(user);
        userStorage.updateUserInStorage(usersFriend);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) throws NotFoundException {
        User user = userStorage.getUserFromStorage(userId);
        User usersFriend = userStorage.getUserFromStorage(friendId);
        if (user.getFriends().contains(usersFriend.getId()) && usersFriend.getFriends().contains(user.getId())) {
            user.getFriends().remove(usersFriend.getId());
            usersFriend.getFriends().remove(user.getId());
            userStorage.updateUserInStorage(user);
            userStorage.updateUserInStorage(usersFriend);
            return user;
        } else {
            throw new NotFoundException("Удалить из друзей пользователя с id = " + usersFriend.getId() + " невозможно");
        }
    }

    public List<User> getFriends(Long userId) throws NotFoundException {
        User user = userStorage.getUserFromStorage(userId);
        Set<Long> userFriendsId = user.getFriends();
        List<User> friendsList = new ArrayList<>();
        for (Long id : userFriendsId) {
            friendsList.add(userStorage.getUserFromStorage(id));
        }
        return friendsList;
    }

    public List<User> getCommonFriends(Long userId, Long friendId) throws NotFoundException {
        User user = userStorage.getUserFromStorage(userId);
        User usersFriend = userStorage.getUserFromStorage(friendId);
        Set<Long> userFriendsId = user.getFriends();
        Set<Long> usersFriendFriendsId = usersFriend.getFriends();
        Set<Long> common = new HashSet<>(userFriendsId);
        common.retainAll(usersFriendFriendsId);
        List<User> commonFriendsList = new ArrayList<>();
        for (Long id : common) {
            commonFriendsList.add(userStorage.getUserFromStorage(id));
        }
        return commonFriendsList;
    }

    private void validateInput(User user) throws ValidationException {
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