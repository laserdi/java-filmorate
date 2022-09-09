package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface FilmStorage {
    
    /**
     * Создание фильма
     *
     * @param film из тела запроса.
     * @return статус состояния на запрос и тело ответа (созданный фильм или ошибка).
     */
    Film createInStorage(Film film);
    
    /**
     * Обновление информации о существующем фильме.
     *
     * @param film обновляемый фильм.
     * @return обновлённый фильм.
     */
    Film updateInStorage(Film film);
    
    /**
     * Удалить фильм из библиотеки.
     * @param film - фильм.
     * @return удалённый пользователь.
     */
    Film removeFromLibrary(Film film);
    
    /**
     * Вывести список всех фильмов.
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
}
