package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;

import java.time.LocalDate;
import java.util.List;

/**
 * Добавьте в классы-контроллеры эндпоинты с подходящим типом запроса для каждого из случаев.
 * <p>
 * Для FilmController:
 * <p>добавление фильма;</p>
 * <p>обновление фильма;</p>
 * <p>получение всех фильмов.</p>
 */
@Slf4j
@Component
@RestController
@RequiredArgsConstructor
public class FilmController {
    FilmService filmService;
    
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    
    static final String PATH_FOR_FILMS = "/films";
    
    /**
     * Получение списка всех фильмов.
     *
     * @return список фильмов.
     */
    @GetMapping(PATH_FOR_FILMS)
    public List<Film> getAllFilms() {
        log.info("Выдан ответ на запрос всех фильмов.");
        return filmService.getAllFilms();
    }
    
    /**
     * Создание фильма
     *
     * @param film из тела запроса.
     * @return статус состояния на запрос и тело ответа (созданный фильм или ошибка).
     */
    @PostMapping(PATH_FOR_FILMS)
    public ResponseEntity<?> createFilm(@RequestBody Film film) {
        Film createdFilm;
        try {
            createdFilm = filmService.createFilm(film);
        } catch (ValidateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (NotFoundRecordInBD ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.ok(createdFilm);
    }
    
    /**
     * Обновление информации о существующем фильме.
     *
     * @param film обновляемый фильм.
     * @return ответ о совершённом действии.
     */
    @PutMapping(PATH_FOR_FILMS)
    public ResponseEntity<?> updateFilm(@RequestBody Film film) {
        Film updatedFilm;
        try {
            updatedFilm = filmService.updateFilm(film);
        } catch (ValidateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (NotFoundRecordInBD ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.ok(updatedFilm);
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
            log.info("chekFilm(): ID фильма = null.");
        }
        
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new ValidateException("chekFilm(): Отсутствует название фильма.");
        }
        
        if (description != null && description.length() > 200) {
            throw new ValidateException("chekFilm(): Максимальная длина описания фильма должна быть не более" +
                    " 200 символов.");
        }
        
        if (releaseDate == null || releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("chekFilm(): Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        
        if (duration != null && duration <= 0) {
            throw new ValidateException("chekFilm(): Продолжительность фильма должна быть положительной.");
        }
    }
}
