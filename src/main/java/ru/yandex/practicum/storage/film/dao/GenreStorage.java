package ru.yandex.practicum.storage.film.dao;

import ru.yandex.practicum.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    
    /**
     * Внесение данных в таблицу фильмов и жанров.
     * @param filmId ID фильма.
     * @param genres список жанров.
     */
    Set<Genre> addInDBFilm_Genre(Integer filmId, Set<Genre> genres);
    
    /**
     * Получить список жанров фильма с ID = idFilm.
     * @param idFilm ID фильма.
     * @return список жанров фильма.
     */
    Set<Genre> findGenresOfFilmId(Integer idFilm);
    
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
    
    /**
     * Проверить наличие жанра в БД по его ID.
     * @param genreId ID жанра.
     * @return True - жанр найден.
     * <p>False - жанра нет в БД.</p>
     */
    boolean existGenreInDBById(Integer genreId);
}
