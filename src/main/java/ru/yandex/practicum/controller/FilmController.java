package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.ValidateException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Добавьте в классы-контроллеры эндпоинты с подходящим типом запроса для каждого из случаев.
 * <p>
 * Для FilmController:
 * <p>добавление фильма;</p>
 * <p>обновление фильма;</p>
 * <p>получение всех фильмов.</p>
 */
@Slf4j
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static Integer count = 0;
    
    @GetMapping("/films")
    public List<Film> findAllFilms() {
        count++;
        log.info("Выдан ответ на запрос всех фильмов.");
        return new ArrayList<>(films.values());
    }
    
    /**
     * Создание фильма
     *
     * @param film из тела запроса.
     * @return статус состояния на запрос и тело ответа (созданный фильм или ошибка).
     */
    @PostMapping("/films")
    public ResponseEntity<?> createFilm(@RequestBody Film film) {
        try {
            checkFilm(film);
            
            if (film.getId() == null) {
                //заполняем сущность необходимыми данными
                film.setId(getUniqueId());
                log.info("Создана новая запись в библиотеке о фильме с названием: '"
                        + film.getName() + "'.");
                films.put(film.getId(), film);
                return ResponseEntity.ok(film);
            } else if (idFilmAlreadyExistInLibrary(film)) {
                log.info("При создании выполнено обновление существующей записи " +
                        "о фильме с ID = '" + films.get(film.getId()).getId()
                        + "' и названием '" + films.get(film.getId()).getName() + "'.");
                films.put(film.getId(), film);
                return ResponseEntity.ok(film);
            } else {
                log.info("Создан фильм из 'полного' объекта в теле запроса. " +
                        "В запросе были все необходимые поля.");
                films.put(film.getId(), film);
                return ResponseEntity.ok(film);
            }
            
            
        } catch (ValidateException ex) {
            log.error("Ошибка добавления фильма в библиотеку. Ошибка: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Ошибка добавления фильма в библиотеку. Ошибка: "
                    + ex.getMessage());
        }
    }
    
    /**
     * Обновление информации о существующем фильме.
     *
     * @param film обновляемый фильм.
     * @return ответ о совершённом действии.
     */
    @PutMapping("/films")
    public ResponseEntity<?> updateFilm(@RequestBody Film film) {
        try {
            checkFilm(film);
            if (film.getId() == null) {
                film.setId(getUniqueId());
                log.info("Фильм \"" + film.getName() + "\" успешно добавлен в библиотеку."
                        + " Вбъекта в теле запроса, отсутствовал ID (он был сгенерирован " +
                        "и присвоен объекту).");
                films.put(film.getId(), film);
                return ResponseEntity.ok(film);
                
            } else if (idFilmAlreadyExistInLibrary(film)) {
                //Получается обновление существующей записи о фильме.
                log.info("Выполнено обновление существующей записи " +
                        "о фильме с ID = '" + films.get(film.getId()).getId()
                        + "' и названием '" + films.get(film.getId()).getName() + "'.");
                films.put(film.getId(), film);
                return ResponseEntity.ok(film);
                
            } else {
                log.info("Ошибка обновления информации о фильме с ID = '"
                        + film.getId() + "', которого нет в библиотеке.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
            }
            
        } catch (ValidateException ex) {
            log.error("Ошибка обновления фильма в библиотеке. Ошибка: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Ошибка добавления фильма в библиотеку. Ошибка: "
                    + ex.getMessage());
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
    
    /**
     * Метод проверки наличия фильма в библиотеке по его ID.
     *
     * @param film фильм, наличие которого необходимо проверить в библиотеке.
     * @return true - фильм присутствует в библиотеке.
     * <p>false - фильма нет в библиотеке.</p>
     */
    private boolean idFilmAlreadyExistInLibrary(Film film) {
        Integer id = film.getId();
        return films.containsKey(id);
    }
    
    /**
     * Метод получения уникального ID для фильма.
     *
     * @return уникальный ID.
     */
    public Integer getUniqueId() {
        while (true) {
            count++;
            if (!films.containsKey(count)) {
                return count;
            }
        }
    }
}
