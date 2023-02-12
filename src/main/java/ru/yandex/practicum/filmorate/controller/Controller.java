package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.util.List;


public abstract class Controller<T> {
    @GetMapping
    public abstract List<T> getAll();

    @GetMapping
    public abstract T get(@PathVariable Long id) throws NotFoundException;

    @PostMapping
    public abstract T create(@Valid @RequestBody T obj) throws ValidationException, AlreadyExistException;

    @PutMapping
    public abstract T update(@Valid @RequestBody T obj) throws ValidationException, NotFoundException;

    @PutMapping
    public abstract T put(@PathVariable Long id1, @PathVariable Long id2) throws NotFoundException;

    @DeleteMapping
    public abstract T delete(@PathVariable Long id1, @PathVariable Long id2) throws NotFoundException;
}
