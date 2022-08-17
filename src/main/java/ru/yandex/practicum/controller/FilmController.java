package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.ValidateException;

import java.time.LocalDate;
import java.util.HashMap;
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
    public Map<Integer, Film> findAllFilms() {
        count++;
        Film filmForTest = Film.builder()
                .id(-5)
                .name("фильм №1")
                .description("описание фильма №1")
                .releaseDate(LocalDate.of(1983, 12, 20))
                .duration(100)
                .build();
        System.out.println(filmForTest);
        //films.put(filmForTest.getId(), filmForTest);
        return films;
    }
    
    @PostMapping("/films")
    public ResponseEntity<String> createFilm(@RequestBody Film film) {
        try {
            checkFilm(film);
            count++;
            if (film.getId() == null) {
                film.setId(count);
            }
            if (isFilmAlreadyExistInLibrary(film)) {
                //Получается обновление существующей записи о фильме.
//                films.put(film.getId(), film);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Выполнено обновление существующей записи о фильме: \"" + film.getName() + "\".");
            }
            
            films.put(film.getId(), film);
            return ResponseEntity.ok("Фильм \"" + film.getName() + "\" успешно добавлен в библиотеку.");
            
        } catch (ValidateException ex) {
            log.error("Ошибка добавления фильма в библиотеку. Ошибка: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Ошибка добавления фильма в библиотеку. Ошибка: "
                    + ex.getMessage());
        }
    }
    
    /**
     * Обновление информации о существующем фильме.
     * @param film обновляемый фильм.
     * @return ответ о совершённом действии.
     */
    @PutMapping("/films")
    public ResponseEntity<String> updateFilm(@RequestBody Film film) {
        try {
            checkFilm(film);
            
            if (isFilmAlreadyExistInLibrary(film)) {
                //Получается обновление существующей записи о фильме.
                films.put(film.getId(), film);
                log.info("Выполнено обновление существующей записи о фильме: \""
                        + film.getName() + "\".");
                return ResponseEntity.ok("Выполнено обновление существующей записи о фильме: \""
                        + film.getName() + "\".");
            }
            
            films.put(film.getId(), film);
            log.info("Фильм \"" + film.getName() + "\" успешно добавлен в библиотеку.");
            return ResponseEntity.ok("Фильм \"" + film.getName() + "\" успешно добавлен в библиотеку.");
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

/*
        if (id == null) {
            throw new InvalidFilmException("Проверьте ID фильма. ID отсутствует.");
        }
*/
        
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new ValidateException("Отсутствует название фильма.");
        }
        
        if (description != null && description.length() > 200) {
            throw new ValidateException("Максимальная длина описания фильма должна быть не более" +
                    " 200 символов.");
        }
        
        if (releaseDate == null || releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
        
        if (duration!= null && duration <= 0) {
            throw new ValidateException("Продолжительность фильма должна быть положительной.");
        }
    }
    
    /**
     * Метод проверки наличия фильма в библиотеке.
     *
     * @param film фильм, наличие которого необходимо проверить в библиотеке.
     * @return true - фильм присутствует в библиотеке.
     * <p>false - фильма нет в библиотеке.</p>
     */
    private boolean isFilmAlreadyExistInLibrary(Film film) {
        Integer id = film.getId();
        return films.containsKey(id);
    }
}
