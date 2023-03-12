package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


public abstract class Controller<T> {
    @GetMapping
    public abstract List<T> getAll();

    @GetMapping
    public abstract T get(@PathVariable Long id);

    @PostMapping
    public abstract T create(@Valid @RequestBody T obj);

    @PutMapping
    public abstract T update(@Valid @RequestBody T obj);

    @PutMapping
    public abstract T put(@PathVariable Long id1, @PathVariable Long id2);

    @DeleteMapping
    public abstract T delete(@PathVariable Long id1, @PathVariable Long id2);
}
