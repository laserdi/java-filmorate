package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface FilmStorage {

    boolean create(Film film);
    boolean update(Film film);
    boolean removeFromLibrary(Film film);
    List<Film> getAllFilms();
    Film getFilmById(Integer id);
    Film getFilmByName(String name);
}
