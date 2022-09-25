package ru.yandex.practicum.storage.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.storage.film.dao.GenreStorage;
import ru.yandex.practicum.storage.mapper.GenreMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@Qualifier("GenreDBStorage")
public class GenreDBStorage implements GenreStorage {
    
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;
    
    @Autowired
    public GenreDBStorage(JdbcTemplate jdbcTemplate, GenreMapper genreMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreMapper = genreMapper;
    }
    
    /**
     * Внесение данных в таблицу жанров и фильмов.
     *
     * @param filmId ID фильма.
     * @param genres список жанров.
     * @return список жанров, запрошенный из БД по тому же фильму.
     */
    @Override
    public Set<Genre> addInDBFilm_Genre(Integer filmId, Set<Genre> genres) {
        log.debug("Вызван метод внесения данных в таблицу жанров и фильмов. " +
                "Список 'входящих' жанров фильма с ID = {}:\t{}", filmId, genres);
        //Удаляем СТАРЫЕ жанры из таблицы для фильма с ID = filmId:
        String sqlForDeleteGenres = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlForDeleteGenres, filmId);
        //Далее добавляем НОВЫЕ жанры в таблицу для фильма с ID = filmId:
        String sqlQuery = "merge into FILM_GENRE(FILM_ID, GENRE_ID) values (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sqlQuery, filmId, genre.getId());
        }
        Set<Genre> result = new HashSet<>(findGenresOfFilmId(filmId));
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
    public Set<Genre> findGenresOfFilmId(Integer filmId) {
        log.info("Вызван метод получения списка жанров фильма с ID = {}.", filmId);
        //String sqlQuery2 = "SELECT * FROM FILMS f JOIN MPA M ON M.MPA_ID = F.MPA_ID ORDER BY f.rate DESC LIMIT ?";
        String sqlQuery = "select * from FILM_GENRE as FG join GENRES G on FG.GENRE_ID = G.GENRE_ID where FILM_ID = ?";
        Set<Genre> result = new HashSet<>(jdbcTemplate.query(sqlQuery, genreMapper, filmId));
        //List<Genre> result2 = jdbcTemplate.query(sqlQuery2, genreMapper, filmId);
        log.info("Получен ответ метода получения списка жанров фильма с ID = {}.", filmId);
        return new HashSet<>(result);
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
        return jdbcTemplate.queryForObject(sqlQuery, genreMapper, genreId);
    }
    
    /**
     * Проверить наличие жанра в БД по его ID.
     * @param genreId ID жанра.
     * @return True - жанр найден.
     * <p>False - жанра нет в БД.</p>
     */
    @Override
    public boolean existGenreInDBById(Integer genreId) {
        String sqlQuery = "select COUNT(*) from GENRES where GENRE_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, genreId);
        if (count!=null) {
            return count.equals(1);
        }
        return false;
    }
}
