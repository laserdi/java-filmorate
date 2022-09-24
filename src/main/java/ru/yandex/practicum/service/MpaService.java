package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.storage.film.dao.MpaStorage;

import java.util.List;

@Service
@Qualifier("MpaDBService")
@Slf4j
public class MpaService {
    
    //***********************************************
    // **********************************************
    // Вроде бы доделал. Надо внедрить его в ValidationService и всё остальное.*
    @Qualifier("MpaDBStorage")
    MpaStorage mpaDBStorage;
    
    public MpaService(MpaStorage mpaDBStorage) {
        this.mpaDBStorage = mpaDBStorage;
    }
    
    /**
     * Получение MPA-рейтинга по его ID.
     *
     * @param mpaId ID рейтинга MPA.
     * @return MPA или null.
     */
    public Mpa getMpaById(Integer mpaId) {
        return mpaDBStorage.getMpaById(mpaId);
    }
    
    /**
     * Получить список всех MPA из БД.
     *
     * @return список всех MPA из БД.
     */
    public List<Mpa> getAllMpa() {
        return mpaDBStorage.getAllMpa();
    }
    
    /**
     * Получить информацию о наличии MPA с ID = 'mpaId'.
     *
     * @param mpaId ID фильма.
     * @return True — рейтинг с таким ID есть в БД.
     * <p>False — рейтинга с таким ID нет в БД.</p>
     */
    public boolean existMpaByIdInDB(Integer mpaId) {
        return mpaDBStorage.existMpaByIdInDB(mpaId);
    }
}
