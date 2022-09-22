package ru.yandex.practicum.storage.film.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.storage.mapper.GenreMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface GenreStorage {
    
    /**
     * Внесение данных в таблицу жанров и фильмов.
     */
    public List<Genre> addInDBFilm_Genre(Integer filmId, Set<Integer> genres);
    
    /**
     * Возвращает список жанров
     * @return
     */
    public List<Genre> findGenresByFilmIds(Integer id);
}
