package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.dao.FilmStorage;
import ru.yandex.practicum.storage.film.impl.FilmDBStorage;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceNew {
    FilmStorage filmDBStorage;
    
    @Autowired
    public FilmServiceNew(FilmStorage filmDBStorage) {
        this.filmDBStorage = filmDBStorage;
    }
    public Film add(Film film) {
        checkFilm(film);
        filmDBStorage.addInStorage(film);
        return film;
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
    
}
