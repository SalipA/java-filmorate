package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;


public abstract class Controller<T> {
    @GetMapping
    public abstract List<T> getAll() throws NotFoundException, SQLException;

    @GetMapping
    public abstract T get(@PathVariable Long id) throws NotFoundException, SQLException, AlreadyExistException;

    @PostMapping
    public abstract T create(@Valid @RequestBody T obj) throws ValidationException, AlreadyExistException,
        NotFoundException;

    @PutMapping
    public abstract T update(@Valid @RequestBody T obj) throws ValidationException, NotFoundException,
        AlreadyExistException;

    @PutMapping
    public abstract T put(@PathVariable Long id1, @PathVariable Long id2) throws NotFoundException, SQLException,
        AlreadyExistException;

    @DeleteMapping
    public abstract T delete(@PathVariable Long id1, @PathVariable Long id2) throws NotFoundException, SQLException,
        AlreadyExistException;
}
