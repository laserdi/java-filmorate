package ru.yandex.practicum.storage.film.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.storage.film.dao.GenreStorage;
import ru.yandex.practicum.storage.mapper.GenreMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GenreDBStorage implements GenreStorage {
    
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private final GenreMapper genreMapper = new GenreMapper();
    
    /**
     * Внесение данных в таблицу жанров и фильмов.
     */
    @Override
    public List<Genre> addInDBFilm_Genre(Integer filmId, Set<Integer> genres) {
        List<Genre> result = new ArrayList<>();
        String sqlQuery = "MERGE INTO FILM_GENRE" +
                "(film_id, genre_id) values (?, ?)";
        if (genres == null) {
            return null;
        }
        genres.forEach(genreId -> jdbcTemplate.update(sqlQuery, filmId, genreId));
        result = findGenresByIds(9);
        return result;
    }
    
    public List<Genre> findGenresByIds(Integer id) {
        return null;
    }
    
    /**
     * Возвращает список жанров
     *
     * @param id
     * @return
     */
    @Override
    public List<Genre> findGenresByFilmIds(Integer id) {
        return null;
    }
}
