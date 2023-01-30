package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class UserValidationTest {
    static UserController UC;
    static Validator validator;
    User testUser;

    @BeforeAll
    public static void setupValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeAll
    public static void createControllers() {
        UC = new UserController();
    }

    @BeforeEach
    public void createTestUser() {
        testUser = new User();
    }

    @Test
    public void annotationEmailTestForUserEmail() {
        testUser.setLogin("testLogin");
        testUser.setEmail("fakeEmail");
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    public void annotationEmailTestForUserEmail2() {
        testUser.setLogin("testLogin");
        testUser.setEmail("это-неправильный?эмейл@");
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    public void shouldThrowExpIfCreateUserEmailIsEmpty() {
        testUser.setLogin("testLogin");
        testUser.setEmail("");
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> UC.create(testUser)
        );
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exp.getMessage());
    }

    @Test
    public void shouldThrowExpIfCreateUserEmailIsFake() {
        testUser.setLogin("testLogin");
        testUser.setEmail("fakeEmail");
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> UC.create(testUser)
        );
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
            exp.getMessage());
    }

    @Test
    public void shouldThrowExpIfCreateUserLoginIsEmpty() {
        testUser.setLogin("");
        testUser.setEmail("test@test.ru");
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> UC.create(testUser)
        );
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exp.getMessage());
    }

    @Test
    public void shouldThrowExpIfCreateUserLoginHasSpaces() {
        testUser.setLogin("te st");
        testUser.setEmail("test@test.ru");
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> UC.create(testUser)
        );
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы", exp.getMessage());
    }

    @Test
    public void shouldCreateUserIfNameIsEmpty() throws ValidationException {
        testUser.setLogin("test");
        testUser.setEmail("test@test.ru");
        testUser.setName("");
        UC.create(testUser);
        Assertions.assertEquals(UC.getAll().get(0).getName(), testUser.getLogin());
    }

    @Test
    public void shouldThrowExpIfCreateUserIfUsersBirthdayInFuture() {
        testUser.setLogin("test");
        testUser.setEmail("test@test.ru");
        testUser.setBirthday(LocalDate.of(2030, 12, 12));
        final ValidationException exp = Assertions.assertThrows(ValidationException.class,
            () -> UC.create(testUser)
        );
        Assertions.assertEquals("Дата рождения не может быть в будущем", exp.getMessage());
    }

    @Test
    public void annotationNotBlankTestForUserLogin() {
        testUser.setEmail("test@test.ru");
        testUser.setLogin(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        Assertions.assertEquals(1, violations.size());
    }
}