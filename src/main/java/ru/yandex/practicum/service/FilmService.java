package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.storage.film.dao.FilmStorage;
import ru.yandex.practicum.storage.film.dao.GenreStorage;
import ru.yandex.practicum.storage.film.dao.MpaStorage;
import ru.yandex.practicum.storage.user.dao.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))         //совместное использование Lombok и Qualifier
@Qualifier("MpaDBService")
public class FilmService {
    private final FilmStorage filmStorage;
    
    private final GenreStorage genreStorage;
    
    private final ValidationService validationService;
    
    @Autowired
    public FilmService(@Qualifier("FilmDBStorage") FilmStorage filmStorage,
                       ValidationService validationService,
                       @Qualifier("GenreDBStorage") GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.validationService = validationService;
        this.genreStorage = genreStorage;
    }
    
    /**
     * Получение списка всех фильмов из библиотеки.
     *
     * @return Список фильмов.
     */
    public List<Film> getAllFilms() {
        List<Film> result = filmStorage.getAllFilms();
        //Каждому фильму присваиваем набор жанров из таблицы 'film_genre'.
        result.forEach(film -> film.setGenres(new HashSet<>(genreStorage.findGenresOfFilmId(film.getId()))));
        log.info("Получен список всех фильмов из библиотеки.");
        return result;
    }
    
    /**
     * Создание фильма.
     *
     * @param film из тела запроса.
     * @return статус состояния на запрос и тело ответа (созданный фильм или ошибка).
     */
    public Film add(Film film) {
        validationService.checkFilm(film);                      //Проверяем фильм.
        filmStorage.addInStorage(film);                         //Добавляем в таблицу 'films'.
        //Добавляем в таблицу 'film_genre'. Сразу же возвращённое значение присваиваем фильму.
        Set<Genre> genres = genreStorage.addInDBFilm_Genre(film.getId(), film.getGenres());
        film.setGenres(genres);
        log.info("В БД добавлен новый фильм:\t\t{}", film);
        return film;
    }
    
    public Film update(Film film) {
        validationService.checkFilm(film);                      //Проверка фильма.
        validationService.checkExistFilmInDB(film.getId());     //проверка наличия в БД. Иначе NotFoundRecordInBD
        filmStorage.updateInStorage(film);                      //обновление данных в БД 'films'.
        Set<Genre> result = genreStorage.addInDBFilm_Genre(film.getId(), film.getGenres());     //добавить в БД 'film_genre'
        film.setGenres(result);                                 //Обновляем жанры входящего фильма.
        log.info("Фильм с {} в БД успешно обновлён фильм:\t\t{}", film.getId(), film);
        return film;
    }
    
    public Film getFilmById(Integer filmId) {
        validationService.checkExistFilmInDB(filmId);
        Film film = filmStorage.getFilmById(filmId);
        film.setGenres(new HashSet<>(genreStorage.findGenresOfFilmId(filmId)));
        log.info("Получение фильма из БД по ID = {}:\t\t{}", filmId, film);
        return film;
    }
    
    public void deleteById(Integer filmId) {
        filmStorage.removeFromStorageById(filmId);
        log.info("Выполнено удаление фильма с ID = {} из БД.", filmId);
    }
    
    /**
     * GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     *
     * @param count необязательный параметр - размер возвращаемого списка фильмов. (если нет, то 10).
     * @return список популярных фильмов.
     */
    
    
}
