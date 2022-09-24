package ru.yandex.practicum.storage.film.dao;

import ru.yandex.practicum.model.Genre;

import java.util.List;

public interface GenreStorage {
    
    /**
     * Внесение данных в таблицу фильмов и жанров.
     * @param filmId ID фильма.
     * @param genres список жанров.
     */
    public List<Genre> addInDBFilm_Genre(Integer filmId, List<Genre> genres);
    
    /**
     * Получить список жанров фильма с ID = idFilm.
     * @param idFilm ID фильма.
     * @return список жанров фильма.
     */
    List<Genre> findGenresOfFilmId(Integer idFilm);
    
    /**
     * Получить все жанры из БД.
     * @return список жанров.
     */
    List<Genre> getAllGenres();
    
    /**
     * Получить жанр из БД по его ID.
     * @param genreId ID жанра.
     * @return жанр Genre(genre_id, name).
     */
    Genre getGenreById(Integer genreId);
}
