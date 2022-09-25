package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.film.dao.FilmStorage;
import ru.yandex.practicum.storage.film.dao.GenreStorage;
import ru.yandex.practicum.storage.film.dao.MpaStorage;
import ru.yandex.practicum.storage.user.dao.UserStorage;

import java.time.LocalDate;

@Service
@Slf4j
public class ValidationService {
    @Qualifier("UserDBStorage")
    private final UserStorage userDBStorage;
    
    @Qualifier("FilmDBStorage")
    private final FilmStorage filmDBStorage;
    
    @Qualifier("MpaDBStorage")
    private final MpaStorage mpaStorage;
    
    @Qualifier("GenreDBStorage")
    private final GenreStorage genreStorage;
    
    @Autowired
    public ValidationService(UserStorage userDBStorage, FilmStorage filmDBStorage, MpaStorage mpaStorage,
                             GenreStorage genreStorage) {
        this.userDBStorage = userDBStorage;
        this.filmDBStorage = filmDBStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }
    
    // **************************************************
    // **************************************************
    // *****                                        *****
    // *****     Методы работы с пользователями     *****
    // *****                                        *****
    // **************************************************
    // **************************************************
    
    /**
     * Проверка удовлетворения полей объекта User требуемым параметрам:
     * <p>электронная почта не может быть пустой и должна содержать символ @;</p>
     * <p>логин не может быть пустым и содержать пробелы;</p>
     * <p>имя для отображения может быть пустым — в таком случае будет использован логин;</p>
     * <p>дата рождения не может быть в будущем.</p>
     * <p>Если 'name' пользователя отсутствует, то ему присваивается значение 'login'.</p>
     *
     * @param user пользователь, которого необходимо проверить.
     * @throws ValidateException в объекте пользователя есть ошибки.
     */
    void checkUser(User user) throws ValidateException {
        Integer id = user.getId();
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();
        
        if (id == null) log.info("checkUser(): ID пользователя = null.");
        
        //электронная почта не может быть пустой и должна содержать символ @;
        if (email == null || email.isEmpty() || email.isBlank()) {
            log.info("checkUser(): Не пройдена проверка 'пустоты' адреса электронной почты.");
            throw new ValidateException("Адрес электронной почты не может быть пустым.");
        }
        if (!email.contains("@")) {
            log.info("checkUser(): Не пройдена проверка наличия '@' в адресе электронной почты.");
            throw new ValidateException("Адрес электронной почты должен содержать символ '@'.");
        }
        
        //логин не может быть пустым и содержать пробелы;
        if (login == null || login.isEmpty() || login.isBlank()) {
            log.info("checkUser(): Не пройдена проверка наличия логина.");
            throw new ValidateException("Отсутствует логин (" + login + ") пользователя.");
        }
        if (login.contains(" ")) {
            log.info("checkUser(): Не пройдена проверка отсутствия пробелов в логине.");
            throw new ValidateException("В логине не должно быть пробелов.");
        }
        
        //дата рождения не может быть в будущем
        if (birthday != null) {
            if (birthday.isAfter(LocalDate.now())) {
                log.info("checkUser(): Не пройдена проверка корректной даты рождения. Дата рождения ещё не наступила");
                throw new ValidateException("checkUser(): Дата рождения ещё не наступила. " +
                        "Введите корректную дату рождения.");
            }
        }
        
        nameSetAsLogin(user);
    }
    
    /**
     * Метод присвоения имени пользователя при его отсутствии.
     * Если имя пустое, то оно равно логину.
     *
     * @param user обрабатываемый пользователь.
     */
    private void nameSetAsLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имени пользователя присвоено значение логина = {}", user.getLogin());
        }
    }
    
    /**
     * Проверка наличия юзера в БД.
     *
     * @param id пользователя.
     * @throws NotFoundRecordInBD - пользователь найден в БД.
     */
    public void checkExistUserInDB(Integer id) {
        if (userDBStorage.isExistUserInDB(id)) {
            return;
        }
        String error = "Error 404. Пользователь с ID = '" + id + "' не найден в БД.";
        log.error(error);
        throw new NotFoundRecordInBD(error);
    }
    
    
    /**
     * <p>*********Метод пока не нужен.*********</p>
     * Метод проверки наличия пользователя в базе данных по логину.
     *
     * @param login пользователь, наличие логина которого необходимо проверить в базе данных.
     * @return ID, найденный в БД по логину.
     * Если возвращается не null, то после этой проверки можно обновлять пользователя,
     * присвоив ему ID из базы данных.
     * <p>null - пользователя нет в базе данных.</p>
     */
    public Integer idFromDBByLogin(String login) {
        if (login == null) {
            return null;
        }
        for (User u : userDBStorage.getAllUsersFromStorage()) {
            if (u.getLogin().equals(login)) {
                return u.getId();
            }
        }
        return null;
    }
    
    // **************************************************
    // **************************************************
    // *******                                   ********
    // *******     Методы работы с фильмами      ********
    // *******                                   ********
    // **************************************************
    // **************************************************
    
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
    public void checkFilm(Film film) throws ValidateException {
        final Integer ID = film.getId();
        final String NAME = film.getName();
        final String DESCRIPTION = film.getDescription();
        final LocalDate RELEASE_DATE = film.getReleaseDate();
        final Integer DURATION = film.getDuration();
        final Mpa MPA = film.getMpa();
        
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
        
        //рейтинг фильма MPA должно быть не mull
        if (MPA == null) {
            throw new ValidateException("checkFilm(): Рейтинг фильма MPA должен быть не mull.");
        }
    }
    
    
    /**
     * Проверка наличия фильма в БД.
     *
     * @param filmId ID фильма.
     * @throws NotFoundRecordInBD - если фильма нет в БД.
     */
    public void checkExistFilmInDB(Integer filmId) {
        if (filmDBStorage.isExistFilmInDB(filmId)) {
            return;
        }
        String error = "Error 404. Фильм с ID = '" + filmId + "' не найден в БД.";
        log.error(error);
        throw new NotFoundRecordInBD(error);
    }
    
    // **************************************************
    // **************************************************
    // *******                                   ********
    // *******       Методы работы с MPA         ********
    // *******                                   ********
    // **************************************************
    // **************************************************
    
    /**
     * Получить информацию о наличии MPA с ID = 'mpaId'.
     *
     * @param mpaId ID фильма.
     * @throws NotFoundRecordInBD — рейтинга с таким ID нет в БД.
     *                            <p>иначе — рейтинг с таким ID есть в БД.</p>
     */
    
    public void checkExistMpaInDB(Integer mpaId) {
        if (!mpaStorage.existMpaByIdInDB(mpaId)) {
            String error = "Error 404. MPA-рейтинг с ID = '" + mpaId + "' не найден в БД.";
            log.info(error);
            throw new NotFoundRecordInBD(error);
        }
    }
    
    // **************************************************
    // **************************************************
    // *******                                   ********
    // *******       Методы работы с Genre       ********
    // *******                                   ********
    // **************************************************
    // **************************************************
    
    
    /**
     * Проверить наличие жанра в БД по его ID.
     *
     * @param genreId ID жанра.
     * @throws NotFoundRecordInBD жанра нет в БД.</p>
     *                            <p>Иначе - жанр найден.</p>
     */
    
    public void checkExistGenreInDB(Integer genreId) {
        if (!genreStorage.existGenreInDBById(genreId)) {
            String error = "Error 404. Жанр с ID = '" + genreId + "' не найден в БД.";
            log.info(error);
            throw new NotFoundRecordInBD(error);
        }
    }
}
