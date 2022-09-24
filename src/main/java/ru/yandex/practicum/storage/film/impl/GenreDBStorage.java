package ru.yandex.practicum.storage.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.storage.film.dao.GenreStorage;
import ru.yandex.practicum.storage.mapper.GenreMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@Qualifier("GenreDBStorage")
@RequiredArgsConstructor
public class GenreDBStorage implements GenreStorage {
    
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;
    
    /**
     * Внесение данных в таблицу жанров и фильмов.
     *
     * @param filmId ID фильма.
     * @param genres список жанров.
     * @return список жанров, запрошенный из БД по тому же фильму.
     */
    @Override
    public List<Genre> addInDBFilm_Genre(Integer filmId, List<Genre> genres) {
        log.debug("Вызван метод внесения данных в таблицу жанров и фильмов. " +
                "Список 'входящих' жанров фильма с ID = {}:\t{}", filmId, genres);
        List<Genre> result = new ArrayList<>();
        String sqlQuery = "merge into FILM_GENRE(FILM_ID, GENRE_ID) values (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sqlQuery, filmId, genre.getId());
        }
        result = findGenresOfFilmId(filmId);
        log.debug("Отработал метод внесения данных в таблицу жанров и фильмов. " +
                "Список 'считанных из БД' жанров о фильме с ID = {}:\t{}", filmId, result);
        return result;
    }
    
    /**
     * Получить список жанров фильма с ID = idFilm.
     *
     * @param filmId ID фильма.
     * @return список жанров фильма.
     */
    @Override
    public List<Genre> findGenresOfFilmId(Integer filmId) {
        log.info("Вызван метод получения списка жанров фильма с ID = {}.", filmId);
        String sqlQuery = "select * from FILM_GENRE as FG " +
                "join GENRES G on FG.GENRE_ID = G.GENRE_ID where FILM_ID = ?";
        List<Genre> result = jdbcTemplate.query(sqlQuery, genreMapper, filmId);
        log.info("Получен ответ метода получения списка жанров фильма с ID = {}.", filmId);
        return result;
    }
    
    
    /**
     * Получить все жанры из БД.
     *
     * @return список жанров.
     */
    @Override
    public List<Genre> getAllGenres() {
        log.info("Вызван метод получения данных о всех жанрах.");
        String sqlQuery = "select * from GENRES";
        List<Genre> result = jdbcTemplate.query(sqlQuery, genreMapper);
        log.debug("Из БД получен ответ о всех жанрах.");
        return result;
    }
    
    /**
     * Получить жанр из БД по его ID.
     *
     * @param genreId ID жанра.
     * @return жанр Genre(genre_id, name).
     */
    @Override
    public Genre getGenreById(Integer genreId) {
        String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        jdbcTemplate.queryForObject(sqlQuery, genreMapper, genreId);
        return null;
    }
}
