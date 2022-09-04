package ru.yandex.practicum.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    
    private Map<Integer, Film> films = new HashMap<>();
    
    
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
    public Film create(Film film) {
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
    public Film removeFromLibrary(Film film) {
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
}
