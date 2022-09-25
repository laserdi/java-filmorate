package ru.yandex.practicum.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.dao.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("InMemoryFilmStorage")       //Используется для однозначности использования классов наследников интерфейса.
public class InMemoryFilmStorage implements FilmStorage {
    
    private final Map<Integer, Film> films = new HashMap<>();
    
    
    /**
     * Получить список всех фильмов.
     *
     * @return список фильмов.
     */
    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
    
    
    /**
     * Добавить фильм в библиотеку.
     *
     * @param film фильм.
     * @return добавленный фильм.
     */
    @Override
    public Film addInStorage(Film film) {
        films.put(film.getId(), film);
        return film;
    }
    
    /**
     * Обновление фильма в библиотеке.
     *
     * @param film обновляемый фильм.
     * @return обновлённый фильм.
     */
    @Override
    public Film updateInStorage(Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }
    
    /**
     * Удалить фильм из библиотеки.
     *
     * @param film фильм.
     * @return удалённый фильм.
     */
    @Override
    public Film removeFromStorage(Film film) {
        if (films.remove(film.getId(), film)) {
            return film;
        }
        return null;
    }
    
    /**
     * Метод получения фильма из библиотеки по его ID.
     *
     * @param id ID фильма, наличие которого необходимо проверить в библиотеке.
     * @return Film - фильм присутствует в библиотеке.
     * <p>null - фильма нет в библиотеке.</p>
     */
    @Override
    public Film getFilmById(Integer id) {
        return films.getOrDefault(id, null);
    }
    
    /**
     * Получить фильм по названию.
     *
     * @param name логин пользователя.
     * @return фильм Film или null.
     */
    @Override
    public Film getFilmByName(String name) {
        return films.values().stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
    }
    
    /**
     * Найти популярные фильмы.
     *
     * @param count число фильмов для результата.
     *              <p>Если null, то весь список вывести.</p>
     */
    @Override
    public List<Film> getPopularFilms(Integer count) {
        // TODO: 2022.09.24 04:33:17 Когда-нибудь доделать. - @Dmitriy_Gaju
        return null;
    }
    
    /**
     * Удалить фильм из библиотеки.
     *
     * @param filmId - ID фильма.
     */
    @Override
    public void removeFromStorageById(Integer filmId) {
        // TODO: 2022.09.25 01:52:49 Когда-нибудь сделать. - @Dmitriy_Gaju
    }
    
    /**
     * Проверка наличия фильма в БД по его ID.
     *
     * @param id фильма.
     * @return True - фильм найден. False - фильма нет в БД.
     */
    @Override
    public boolean isExistFilmInDB(Integer id) {
        // TODO: 2022.09.25 03:05:05 Когда-нибудь сделать. - @Dmitriy_Gaju
        return false;
    }
}
