package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

public class UserServiceTest {
    UserService US;
    User STANDARD_CASE_USER;

    @BeforeEach
    public void createFilmService() {
        US = new UserService(new InMemoryUserStorage());
    }

    @BeforeEach
    public void createStandardUser() {
        STANDARD_CASE_USER = new User();
        STANDARD_CASE_USER.setEmail("test@test.ru");
        STANDARD_CASE_USER.setLogin("testLogin");
        STANDARD_CASE_USER.setBirthday(LocalDate.of(2000, 2, 23));
    }

    @Test
    public void shouldAddUserInStorageStandardCase() {
        User testUser = US.create(STANDARD_CASE_USER);
        Assertions.assertEquals(1L, testUser.getId());
        Assertions.assertEquals(US.getUserById(1L), testUser);
        Assertions.assertEquals(1, US.getAllUsers().size());
    }

    @Test
    public void shouldAddUserInStorageIfUserAlreadyExist() {
        US.create(STANDARD_CASE_USER);
        final AlreadyExistException exp = Assertions.assertThrows(AlreadyExistException.class,
            () -> US.create(STANDARD_CASE_USER)
        );
        Assertions.assertEquals("Пользователь с email <test@test.ru> уже был добавлен. Id = 1",
            exp.getMessage());
    }

    @Test
    public void shouldUpdateUserInStorageStandardCase() {
        User testUser = US.create(STANDARD_CASE_USER);
        testUser.setName("Update Name");
        US.update(testUser);
        Assertions.assertEquals("Update Name", testUser.getName());
        Assertions.assertEquals(US.getUserById(1L), testUser);
        Assertions.assertEquals(1, US.getAllUsers().size());
    }

    @Test
    public void shouldUpdateUserInStorageIfUserIdNull() {
        final NotFoundException exp = Assertions.assertThrows(NotFoundException.class,
            () -> US.update(STANDARD_CASE_USER)
        );
        Assertions.assertEquals("Пользователь с id = null не найден", exp.getMessage());
    }

    @Test
    public void shouldUpdateUserInStorageIfUserNotFound() {
        User testUser = US.create(STANDARD_CASE_USER);
        testUser.setName("Update Name");
        testUser.setId(3L);
        final NotFoundException exp = Assertions.assertThrows(NotFoundException.class,
            () -> US.update(testUser)
        );
        Assertions.assertEquals("Пользователь с id = 3 не найден", exp.getMessage());
    }

    @Test
    public void shouldGetAllUsersFromStorageStandardCase() {
        User testUser1 = US.create(STANDARD_CASE_USER);
        User testUser2 = new User();
        testUser2.setEmail("test2@test.ru");
        testUser2.setLogin("testLogin2");
        testUser2.setBirthday(LocalDate.of(2000, 2, 24));
        US.create(testUser2);
        List<User> testList = List.of(testUser1, testUser2);
        Assertions.assertEquals(2, US.getAllUsers().size());
        Assertions.assertEquals(testList, US.getAllUsers());
    }

    @Test
    public void shouldGetUserByIdStandardCase() {
        US.create(STANDARD_CASE_USER);
        STANDARD_CASE_USER.setId(1L);
        Assertions.assertEquals(STANDARD_CASE_USER, US.getUserById(1L));
    }

    @Test
    public void shouldGetUserByIdIfIdNotFound() {
        final NotFoundException exp = Assertions.assertThrows(NotFoundException.class,
            () -> US.getUserById(3L)
        );
        Assertions.assertEquals("Пользователь с id = 3 не найден", exp.getMessage());
    }

    @Test
    public void shouldAddFriendToUserStandardCase() {
        US.create(STANDARD_CASE_USER);
        User testUser2 = new User();
        testUser2.setEmail("test2@test.ru");
        testUser2.setLogin("testLogin2");
        testUser2.setBirthday(LocalDate.of(2000, 2, 24));
        US.create(testUser2);
        US.addFriend(1L, 2L);
        Assertions.assertEquals(1, US.getFriends(1L).size());
    }

    @Test
    public void shouldRemoveFriendsStandardCase() {
        US.create(STANDARD_CASE_USER);
        User testUser2 = new User();
        testUser2.setEmail("test2@test.ru");
        testUser2.setLogin("testLogin2");
        testUser2.setBirthday(LocalDate.of(2000, 2, 24));
        US.create(testUser2);
        US.addFriend(1L, 2L);
        US.removeFriend(1L, 2L);
        Assertions.assertEquals(0, US.getFriends(1L).size());
        Assertions.assertEquals(0, US.getFriends(2L).size());
    }

    @Test
    public void shouldGetFriendsByIdStandardCase() {
        US.create(STANDARD_CASE_USER);
        User testUser2 = new User();
        testUser2.setEmail("test2@test.ru");
        testUser2.setLogin("testLogin2");
        testUser2.setBirthday(LocalDate.of(2000, 2, 24));
        US.create(testUser2);
        US.addFriend(1L, 2L);
        Assertions.assertEquals(1, US.getFriends(1L).size());
        Assertions.assertEquals(US.getFriends(1L), List.of(US.getUserById(2L)));
    }

    @Test
    public void shouldGetCommonFriendsByIdStandardCase() {
        US.create(STANDARD_CASE_USER);
        User testUser2 = new User();
        testUser2.setEmail("test2@test.ru");
        testUser2.setLogin("testLogin2");
        testUser2.setBirthday(LocalDate.of(2000, 2, 24));
        US.create(testUser2);
        User testUser3 = new User();
        testUser3.setEmail("test3@test.ru");
        testUser3.setLogin("testLogin3");
        testUser3.setBirthday(LocalDate.of(2000, 2, 25));
        US.create(testUser3);
        US.addFriend(1L, 3L);
        US.addFriend(2L, 3L);
        Assertions.assertEquals(1, US.getCommonFriends(1L, 2L).size());
        Assertions.assertEquals(List.of(US.getUserById(3L)), US.getCommonFriends(1L, 2L));
    }

    @Test
    public void shouldGetCommonFriendsByIdIfRemoveFriend() {
        US.create(STANDARD_CASE_USER);
        User testUser2 = new User();
        testUser2.setEmail("test2@test.ru");
        testUser2.setLogin("testLogin2");
        testUser2.setBirthday(LocalDate.of(2000, 2, 24));
        US.create(testUser2);
        User testUser3 = new User();
        testUser3.setEmail("test3@test.ru");
        testUser3.setLogin("testLogin3");
        testUser3.setBirthday(LocalDate.of(2000, 2, 25));
        US.create(testUser3);
        US.addFriend(1L, 3L);
        US.addFriend(2L, 3L);
        US.removeFriend(1L, 3L);
        Assertions.assertEquals(0, US.getCommonFriends(1L, 2L).size());
    }
}