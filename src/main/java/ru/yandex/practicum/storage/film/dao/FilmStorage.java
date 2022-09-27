package ru.yandex.practicum.storage.film.dao;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface FilmStorage {
    
    /**
     * Создание фильма в хранилище.
     *
     * @param film объект фильма.
     * @return статус состояния на запрос и тело ответа (созданный фильм или ошибка).
     */
    Film addInStorage(Film film);
    
    /**
     * Обновление информации о существующем фильме.
     *
     * @param film обновляемый фильм.
     * @return обновлённый фильм.
     */
    Film updateInStorage(Film film);
    
    /**
     * Удалить фильм из библиотеки.
     *
     * @param film - фильм.
     * @return удалённый фильм.
     */
    Film removeFromStorage(Film film);
    
    /**
     * Удалить фильм из библиотеки.
     *
     * @param filmId - ID фильма.
     */
    void removeFromStorageById(Integer filmId);
    
    /**
     * Вывести список всех фильмов.
     *
     * @return список фильмов.
     */
    List<Film> getAllFilms();
    
    /**
     * Метод получения фильма из библиотеки по его ID.
     *
     * @param id ID фильма, наличие которого необходимо проверить в библиотеке.
     * @return Film - фильм присутствует в библиотеке.
     * <p>null - фильма нет в библиотеке.</p>
     */
    
    Film getFilmById(Integer id);
    
    Film getFilmByName(String name);
    
    /**
     * Найти популярные фильмы.
     *
     * @param count число фильмов для результата.
     *              <p>Если null, то весь список вывести.</p>
     */
    List<Film> getPopularFilms(Integer count);
    
    /**
     * Проверка наличия фильма в БД по его ID.
     * @param id фильма.
     * @return True - фильм найден. False - фильма нет в БД.
     */
    boolean isExistFilmInDB(Integer id);
}
