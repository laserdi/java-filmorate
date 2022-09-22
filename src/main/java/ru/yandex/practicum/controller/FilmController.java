package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.service.FilmServiceNew;

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
    FilmServiceNew filmServiceNew;
    @Autowired
    public FilmController(FilmServiceNew filmServiceNew, FilmService filmService) {
        this.filmService = filmService;
        this.filmServiceNew = filmServiceNew;
    }
    
    static final String PATH_FOR_FILMS = "/films";
    private static final String PATH_FOR_POPULAR = "/popular";
    private static final String PATH_FOR_LIKE = "/like";
    private static final String PATH_FOR_ID_VARIABLE = "/{id}";
    private static final String PATH_FOR_USER_ID_VARIABLE = "/{userId}";
    
    
    // TODO: 2022.09.20 11:25:51 Проверка работы FilmDBStorage - @Dmitriy_Gaju
    @PostMapping("/filmstest")
    public ResponseEntity<?> addTest(@RequestBody Film film) {
        Film addedFilm = filmServiceNew.add(film);
        return ResponseEntity.ok(addedFilm);
    }
    
    
    
    /**
     * Получение списка всех фильмов.
     *
     * @return список фильмов.
     */
    @GetMapping("/films")
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
    @PostMapping("/films")
    public ResponseEntity<?> createFilm(@RequestBody Film film) {
        return ResponseEntity.ok(filmService.createFilm(film));
    }
    
    /**
     * Обновление информации о существующем фильме.
     *
     * @param film обновляемый фильм.
     * @return ответ о совершённом действии.
     */
    @PutMapping("/films")
    public ResponseEntity<?> updateFilm(@RequestBody Film film) {
        return ResponseEntity.ok(filmService.updateFilm(film));
    }
    
    /**
     * PUT /films/{id}/like/{userId}
     *
     * @param id ID запрашиваемого фильма.
     * @return фильм или исключение 'NotFoundRecordInBD'.
     */
    @GetMapping("/films" + "/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("Выдан ответ на запрос фильма по ID.");
        return filmService.getFilmByID(id);
    }
    
    /**
     * PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
     *
     * @param id     ID фильма, которому ставится лайк.
     * @param userId ID пользователя, ставящего лайк.
     * @return сообщение об успешной установке лайка или генерация исключения 'NotFoundRecordInBD'.
     */
    @PutMapping("/films/{id}/like/{userId}")
    public ResponseEntity<?> addLikeForFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLikeForFilm(id, userId);
        String message = "Лайк фильму (ID = " + id + ") установлен пользователем (ID = " + userId + ").";
        log.info(message);
        return ResponseEntity.ok(message);
    }
    
    
    /**
     * Метод для удаления лайка фильму.
     * DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
     *
     * @param id     ID пользователя, удаляющего лайк.
     * @param userId ID фильма, которому поставили лайк.
     */
    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<?> deleteLikeForFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLikeForFilm(id, userId);
        String message = "Пользователем (ID = " + userId + ") выполнено удаление лайка фильму (ID = " + id + ").";
        log.info(message);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(message);
    }
    
    /**
     * GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     *
     * @param count необязательный параметр - размер возвращаемого списка фильмов. (если нет, то 10).
     * @return список популярных фильмов.
     */
    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        List<Film> popularFilms = filmService.getPopularFilm(count);
        log.info("Выдан ответ на запрос о выдаче списка популярных фильмов.");
        return popularFilms;
    }
}
