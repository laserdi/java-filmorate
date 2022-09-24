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
     * Получить MPA фильма с ID = idFilm.
     * @param idFilm ID фильма.
     * @return список рейтингов фильма.
     */
    boolean existMpaByIdInDB(Integer idFilm);
    
    /**
     * Получить список всех MPA из БД.
     * @return список всех MPA из БД.
     */
    List<Mpa> getAllMpa();
}
