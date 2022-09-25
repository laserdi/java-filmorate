package ru.yandex.practicum.storage.like.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.like.dao.LikeStorage;
import ru.yandex.practicum.storage.mapper.FilmMapper;

import java.util.List;

@Slf4j
@Repository
@Qualifier("LikeDBStorage")
@RequiredArgsConstructor
public class LikeDBStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    /**
     * Пользователь снимает ранее поставленный лайк фильму. Потом ставит новый.
     *
     * @param filmId ID фильма.
     * @param userId ID юзера.
     */
    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQueryForDelete = "delete from FILM_LIKE where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQueryForDelete, filmId, userId);
        String sqlQueryForAdd = "merge into FILM_LIKE (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQueryForAdd, filmId, userId);
        log.info("Пользователем ID = {} поставлен лайк фильму с ID = {}.", userId, filmId);
    }
    
    /**
     * Пользователь удаляет лайк фильму.
     *
     * @param filmId ID фильма.
     * @param userId ID юзера.
     */
    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sqlQueryForDelete = "delete from FILM_LIKE where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQueryForDelete, filmId, userId);
        log.info("Пользователем ID = {} удалён лайк фильму с ID = {}.", userId, filmId);
    }
    
    /**
     * Возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     *
     * @param count число фильмов для результата.
     */
    @Override
    public List<Film> getPopularFilm(Integer count) {
        String sqlQuery = "select F.*, M.* FROM FILMS AS F LEFT JOIN FILM_LIKE AS FL ON f.FILM_ID = FL.FILM_ID " +
                "left join MPA as M ON F.mpa_id = M.mpa_id" +
                " group by F.FILM_ID order by COUNT(FL.user_id) desc limit ?";
        return jdbcTemplate.query(sqlQuery, filmMapper, count);
    }
}
