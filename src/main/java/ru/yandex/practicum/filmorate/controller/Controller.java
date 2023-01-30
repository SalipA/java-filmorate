package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Controller<T> {
    protected final Map<Integer, T> allValues = new HashMap<>();
    protected int counter = 0;

    @GetMapping
    public List<T> getAll() {
        return new ArrayList<>(allValues.values());
    }

    @PostMapping
    public abstract T create(@Valid @RequestBody T obj) throws ValidationException;

    @PutMapping
    public abstract T update(@Valid @RequestBody T obj) throws ValidationException;
}
