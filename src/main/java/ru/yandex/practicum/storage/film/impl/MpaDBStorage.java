package ru.yandex.practicum.storage.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.storage.film.dao.MpaStorage;
import ru.yandex.practicum.storage.mapper.MpaMapper;

import java.util.List;

@Repository
@Qualifier("MpaDBStorage")
@Slf4j
@RequiredArgsConstructor
public class MpaDBStorage implements MpaStorage {
    
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;
    
    /**
     * Получение MPA-рейтинга по его ID.
     *
     * @param mpaId ID рейтинга MPA.
     * @return MPA или null.
     */
    @Override
    public Mpa getMpaById(Integer mpaId) {
        log.debug("Вызван метод получения MPA-рейтинга по его ID = {}.", mpaId);
        String sqlQuery = "select * from MPA where MPA_ID = ?";
        Mpa result = jdbcTemplate.queryForObject(sqlQuery, mpaMapper, mpaId);
        log.debug("Выполнен метод получения MPA-рейтинга по его ID = {}.{}", mpaId, result);
        return result;
    }
    
    /**
     * Получить список всех MPA из БД.
     *
     * @return список всех MPA из БД.
     */
    @Override
    public List<Mpa> getAllMpa() {
        log.debug("Запрос получения списка всех MPA из БД.");
        String sqlQuery = "select * from MPA";
        List<Mpa> result = jdbcTemplate.query(sqlQuery, mpaMapper);
        log.debug("Получен ответ на запрос список всех MPA из БД. {}", result);
        return result;
    }
    
    /**
     * Получить информацию о наличии MPA с ID = 'mpaId'.
     *
     * @param mpaId ID фильма.
     * @return True — рейтинг с таким ID есть в БД.
     * <p>False — рейтинга с таким ID нет в БД.</p>
     */
    @Override
    public boolean existMpaByIdInDB(Integer mpaId) {
        log.debug("Вызван метод проверки наличия в БД MPA-рейтинга с ID = {}.", mpaId);
        String sqlQuery = "select COUNT(*) from MPA where MPA_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, mpaId);
        log.debug("Количество записей в БД MPA-рейтинга с ID = {} равно '{}'.", result, mpaId);
        return result != null && result.equals(1);
    }
}
