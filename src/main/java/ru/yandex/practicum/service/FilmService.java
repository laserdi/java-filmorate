package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    FilmStorage inMemoryFilmStorage;
    
    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }
    
    /**
     * Получение списка всех фильмов из библиотеки.
     *
     * @return Список фильмов.
     */
    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }
    
    /**
     * Создание фильма.
     *
     * @param film из тела запроса.
     * @return статус состояния на запрос и тело ответа (созданный фильм или ошибка).
     */
    public Film createFilm(Film film) throws NotFoundRecordInBD, ValidateException {
        // TODO: 2022.08.28 14:09:17 Нужны ли исключения в сигнатуре? чтобы проверить код ответа при ошибке - @Dmitriy_Gaju
        // TODO: 2022.09.04 03:28:37 Не работает контроллер обработки ошибок. - @Dmitriy_Gaju
        try {
            //Проверяем необходимые поля.
            checkFilm(film);
            
            if (setUniqueIdForFilmFromCount(film)) {
                //Если ID входящего фильма был присвоен, значит фильма нет в библиотеке
                log.info("Создана новая запись в библиотеке о фильме с названием: '"
                        + film.getName() + "' и ID = " + film.getId() + ".");
                return inMemoryFilmStorage.create(film);
                
            } else if (inMemoryFilmStorage.getFilmById(film.getId()) != null) {
                //В библиотеке есть фильм с ID входящего фильма.
                log.info("При создании выполнено обновление существующей записи о фильме с ID = '"
                        + getFilmByID(film.getId()).getId() + "' и названием '"
                        + inMemoryFilmStorage.getFilmById(film.getId()).getName() + "'.");
                return inMemoryFilmStorage.updateInStorage(film);
                
            } else {
                //Поступил фильм со всеми входными параметрами, который был добавлен в библиотеку.
                log.info("Создана новая запись о фильме из 'полного' объекта в теле запроса. " +
                        "В запросе были все необходимые поля.");
                return inMemoryFilmStorage.create(film);
            }
            
        } catch (ValidateException ex) {
            // TODO: 2022.08.28 14:27:21 Проверить, нужен ли здесь try-catch - @Dmitriy_Gaju
            // TODO: 2022.09.04 03:29:26 Не работает контроллер обработки ошибок. - @Dmitriy_Gaju
            String error = "Ошибка добавления фильма в библиотеку. Подробное описание ошибки:\t"
                    + ex.getMessage();
            log.error(error);
            throw new ValidateException(error);
        }
    }
    
    /**
     * Обновить фильм в базе данных.
     *
     * @param film фильм
     * @return обновлённый фильм.
     */
    public Film updateFilm(Film film) {
        try {
            //Проверяем необходимые поля.
            checkFilm(film);
            
            if (setUniqueIdForFilmFromCount(film)) {
                //Если ID входящего фильма был присвоен, значит фильма нет в библиотеке
                log.info("При обновлении создана новая запись в библиотеке о фильме с названием: '"
                        + film.getName() + "'.");
                return inMemoryFilmStorage.create(film);
                
            } else if (inMemoryFilmStorage.getFilmById(film.getId()) != null) {
                //В библиотеке есть фильм с ID входящего фильма.
                log.info("При обновлении выполнено обновление существующей записи о фильме с ID = '"
                        + getFilmByID(film.getId()).getId() + "' и названием '"
                        + inMemoryFilmStorage.getFilmById(film.getId()).getName() + "'.");
                return inMemoryFilmStorage.updateInStorage(film);
            }
            //Во входящем фильме есть ID, но этого ID нет в библиотеке.
            //Получается ошибка в запросе обновления. Нет фильма с указанным ID.
            String error = "Ошибка обновления информации о фильме с ID = '"
                    + film.getId() + "', которого нет в библиотеке.";
            log.error(error);
            throw new NotFoundRecordInBD(error);
            
        } catch (ValidateException ex) {
            String error = "Ошибка обновления фильма в библиотеке. Подробное описание ошибки:\t" + ex.getMessage();
            log.error(error);
            throw new ValidateException(error);
        }
    }
    
    
    /**
     * Для Film:
     * <p>название не может быть пустым;</p>
     * <p>максимальная длина описания — 200 символов;</p>
     * <p>дата релиза — не раньше 28 декабря 1895 года;</p>
     * <p>продолжительность фильма должна быть положительной.</p>
     *
     * @param film фильм, который необходимо проверить.
     * @throws ValidateException в объекте фильма есть ошибки.
     */
    public void checkFilm(Film film) throws ValidateException {
        Integer id = film.getId();
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();
        
        if (id == null) {
            log.info("checkFilm(): ID фильма = null.");
        }
        //название не может быть пустым;
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new ValidateException("checkFilm(): Отсутствует название фильма.");
        }
        //максимальная длина описания — 200 символов;
        if (description != null && description.length() > 200) {
            throw new ValidateException("checkFilm(): Максимальная длина описания фильма должна быть не более" +
                    " 200 символов.");
        }
        //дата релиза — не раньше 28 декабря 1895 года;
        if (releaseDate != null && releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("checkFilm(): Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        //продолжительность фильма должна быть положительной.
        if (duration != null && duration <= 0) {
            throw new ValidateException("checkFilm(): Продолжительность фильма должна быть положительной.");
        }
    }
    
    /**
     * Метод получения фильма из библиотеки по его ID.
     *
     * @param id ID фильма, наличие которого необходимо проверить в библиотеке.
     * @return Film - фильм присутствует в библиотеке.
     * <p>null - фильма нет в библиотеке.</p>
     */
    private Film getFilmByID(Integer id) {
        return inMemoryFilmStorage.getFilmById(id);
    }
    
    
    /**
     * Метод проверки наличия фильма в библиотеке по названию фильма.
     * <p>Этот метод скорее всего не нужен, поскольку в БД могут быть одинаковые названия фильмов.</p>
     *
     * @param film фильм, наличие названия которого необходимо проверить в базе данных.
     * @return ID, найденный в БД по названию.
     * Если возвращается не null, то после этой проверки можно обновлять фильм,
     * присвоив ему ID из базы данных.
     * <p>null - фильма нет в базе данных.</p>
     */
    private Integer idFromDBByName(Film film) {
        String name = film.getName();
        return inMemoryFilmStorage.getAllFilms().stream().filter(f -> f.getName().equals(name))
                .findFirst().map(Film::getId)
                .orElse(null);
    }
    
    
    /**
     * Метод присвоения фильму уникального ID, если ID не задан.
     *
     * @param film обрабатываемый фильм.
     * @return True - уникальный ID присвоен.
     * <p>False - уникальный ID у фильма был.</p>
     */
    public boolean setUniqueIdForFilmFromCount(Film film) {
        if (film.getId() != null) {
            log.info("Уникальный ID фильму не нужен. Изначальный ID = " + film.getId());
            return false;
        }
        while (true) {
            Integer count = Film.getCount();
            if (inMemoryFilmStorage.getFilmById(count) == null) {
                film.setId(count);
                log.info("Уникальный ID фильму присвоен. ID = " + film.getId());
                return true;
            }
        }
    }
}
