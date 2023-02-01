package ru.yandex.practicum.filmorate.service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserLoginConstraintValidator implements ConstraintValidator<UserLoginConstraint, String> {
    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        return login != null && !login.isEmpty() && !login.isBlank() && !login.contains(" ");
    }
}
