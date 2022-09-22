package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.film.dao.FilmStorage;
import ru.yandex.practicum.storage.film.impl.FilmDBStorage;
import ru.yandex.practicum.storage.user.dao.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;
    
    @Value("23245")
    Integer x;
    
    @Autowired
    public FilmService(@Qualifier("FilmDBStorage") FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
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
        try {
            //Проверяем необходимые поля.
            checkFilm(film);
        } catch (ValidateException ex) {
            String error = "Ошибка добавления фильма в библиотеку. Подробное описание ошибки:\t"
                    + ex.getMessage();
            log.error(error);
            throw new ValidateException(error);
        }
        
        if (setUniqueIdForFilmFromCount(film)) {
            //Если ID входящего фильма был присвоен, значит фильма нет в библиотеке
            log.info("Создана новая запись в библиотеке о фильме с названием: '"
                    + film.getName() + "' и ID = " + film.getId() + ".");
            return inMemoryFilmStorage.addInStorage(film);
            
        } else if (inMemoryFilmStorage.getFilmById(film.getId()) != null) {
            //В библиотеке есть фильм с ID входящего фильма.
            log.info("При создании выполнено обновление существующей записи о фильме с ID = '"
                    + getFilmByIDPrivate(film.getId()).getId() + "' и названием '"
                    + inMemoryFilmStorage.getFilmById(film.getId()).getName() + "'.");
            return inMemoryFilmStorage.updateInStorage(film);
            
        } else {
            //Поступил фильм со всеми входными параметрами, который был добавлен в библиотеку.
            log.info("Создана новая запись о фильме из 'полного' объекта в теле запроса. " +
                    "В запросе были все необходимые поля.");
            return inMemoryFilmStorage.addInStorage(film);
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
                return inMemoryFilmStorage.addInStorage(film);
                
            } else if (inMemoryFilmStorage.getFilmById(film.getId()) != null) {
                //В библиотеке есть фильм с ID входящего фильма.
                log.info("При обновлении выполнено обновление существующей записи о фильме с ID = '"
                        + getFilmByIDPrivate(film.getId()).getId() + "' и названием '"
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
     * Метод для установки лайков фильму.
     *
     * @param id     ID пользователя, поставившего лайк.
     * @param idFilm ID фильма, которому поставили лайк.
     */
    public void addLikeForFilm(Integer idFilm, Integer id) {
        User user = inMemoryUserStorage.getUserById(id);
        Film film = inMemoryFilmStorage.getFilmById(idFilm);
        
        if (user != null && film != null) {
            film.getLikes().add(id);
            inMemoryFilmStorage.updateInStorage(film);
        } else {
            String error = "При установке лайка фильму в БД не найден(ы) пользователь и/или фильм."
                    + "Проверьте передаваемые значения ID фильма и пользователя.";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
    }
    
    /**
     * Метод для удаления лайка фильму.
     *
     * @param id     ID пользователя, удаляющего лайк.
     * @param idFilm ID фильма, которому поставили лайк.
     */
    public void deleteLikeForFilm(Integer idFilm, Integer id) {
        User user = inMemoryUserStorage.getUserById(id);
        Film film = inMemoryFilmStorage.getFilmById(idFilm);
        
        if (film != null && user != null) {
            if (film.getLikes().contains(id)) {
                //Если в лайках есть ID пользователя.
                film.getLikes().remove(id);
                inMemoryFilmStorage.updateInStorage(film);
                log.info("Лайк фильму (ID = " + idFilm + ") установлен пользователем (ID = " + id + ").");
            } else {
                String error = "При удалении лайка фильму (ID = " + idFilm
                        + ") выяснилось, что пользователь (ID = " + id + ") не ставил лайк этому фильму.";
                log.error(error);
                throw new NotFoundRecordInBD(error);
            }
        } else {
            String error = "При удалении лайка фильму в БД не найден(ы) пользователь и/или фильм."
                    + "Проверьте передаваемые значения ID фильма и пользователя.";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
    }
    
    /**
     * Получить список самых популярных фильмов (больше лайков).
     *
     * @param count необязательный параметр - размер возвращаемого списка фильмов. (если нет, то 10).
     * @return список популярных фильмов.
     */
    public List<Film> getPopularFilm(Integer count) {
        int size = Objects.requireNonNullElse(count, 10); //Если count = null, тогда size = 10
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(f -> -f.getLikes().size()))
                .limit(size)
                .collect(Collectors.toList());
    }
    
    /**
     * Проверка удовлетворения полей объекта Film требуемым параметрам:
     * <p>* название не может быть пустым;</p>
     * <p>* максимальная длина описания — 200 символов;</p>
     * <p>* дата релиза — не раньше 28 декабря 1895 года;</p>
     * <p>* продолжительность фильма должна быть положительной.</p>
     *
     * @param film фильм, который необходимо проверить.
     * @throws ValidateException в объекте фильма есть ошибки.
     */
    private void checkFilm(Film film) throws ValidateException {
        final Integer ID = film.getId();
        final String NAME = film.getName();
        final String DESCRIPTION = film.getDescription();
        final LocalDate RELEASE_DATE = film.getReleaseDate();
        final Integer DURATION = film.getDuration();
        
        if (ID == null) {
            log.info("checkFilm(): ID фильма = null.");
        }
        //название не может быть пустым;
        if (NAME == null || NAME.isEmpty() || NAME.isBlank()) {
            throw new ValidateException("checkFilm(): Отсутствует название фильма.");
        }
        //максимальная длина описания — 200 символов;
        if (DESCRIPTION != null && DESCRIPTION.length() > 200) {
            throw new ValidateException("checkFilm(): Максимальная длина описания фильма должна быть не более" +
                    " 200 символов.");
        }
        //дата релиза — не раньше 28 декабря 1895 года;
        if (RELEASE_DATE != null && RELEASE_DATE.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("checkFilm(): Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        //продолжительность фильма должна быть положительной.
        if (DURATION != null && DURATION <= 0) {
            throw new ValidateException("checkFilm(): Продолжительность фильма должна быть положительной.");
        }
    }
    
    
    /**
     * ПРИВАТНЫЙ метод получения фильма из библиотеки по его ID.
     *
     * @param id ID фильма, наличие которого необходимо проверить в библиотеке.
     * @return Film - фильм присутствует в библиотеке.
     * <p>null - фильма нет в библиотеке.</p>
     */
    private Film getFilmByIDPrivate(Integer id) {
        return inMemoryFilmStorage.getFilmById(id);
    }
    
    /**
     * ПУБЛИЧНЫЙ метод получения фильма из библиотеки по его ID.
     *
     * @param id ID фильма, наличие которого необходимо проверить в библиотеке.
     * @return Film - фильм присутствует в библиотеке.
     * <p>null - фильма нет в библиотеке.</p>
     */
    public Film getFilmByID(Integer id) {
        Film result = inMemoryFilmStorage.getFilmById(id);
        if (result == null) {
            String error = "Отсутствует фильм в БД при запросе фильма из БД по его ID = "
                    + id + ". Проверьте запрашиваемые параметры.";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
        return result;
    }
    
    
    /**
     * Метод присвоения фильму уникального ID, если ID не задан.
     *
     * @param film обрабатываемый фильм.
     * @return True - уникальный ID присвоен.
     * <p>False - уникальный ID у фильма был.</p>
     */
    private boolean setUniqueIdForFilmFromCount(Film film) {
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
