package ru.yandex.practicum.storage.film.dao;

import ru.yandex.practicum.model.Mpa;

import java.util.List;

public interface MpaStorage {
    
    /**
     * Вернуть MPA-рейтинг по его ID.
     * @param mpaId ID рейтинга MPA.
     * @return MPA или null.
     */
    Mpa getMpaById(Integer mpaId);
    
    /**
     * Получить информацию о наличии MPA с ID = 'mpaId'.
     *
     * @param mpaId ID фильма.
     * @return True — рейтинг с таким ID есть в БД.
     * <p>False — рейтинга с таким ID нет в БД.</p>
     */
    boolean existMpaByIdInDB(Integer mpaId);
    
    /**
     * Получить список всех MPA из БД.
     * @return список всех MPA из БД.
     */
    List<Mpa> getAllMpa();
}
