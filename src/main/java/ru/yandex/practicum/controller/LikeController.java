package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.LikeService;

import java.util.List;

@RestController
@Slf4j
@Component
public class LikeController {
    @Qualifier("LikeDBService")
    private final LikeService likeService;
    
    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }
    
    /**
     * Установка лайка фильму.
     *
     * @param filmId ID фильма.
     * @param userId ID юзера.
     */
    @PutMapping("/films/{id}/like/{userId}")
    public ResponseEntity<?> addLike(
            @PathVariable("id") Integer filmId,
            @PathVariable Integer userId) {
        
        likeService.addLike(filmId, userId);
        return ResponseEntity.ok("Пользователем ID = " + userId + " поставлен лайк фильму с ID = " + filmId + ".");
    }
    
    /**
     * Метод для удаления лайка фильму.
     * DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
     *
     * @param userId     ID пользователя, удаляющего лайк.
     * @param filmId ID фильма, которому поставили лайк.
     */
    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<?> removeLike(@PathVariable("id") Integer filmId,
                                        @PathVariable Integer userId) {
        likeService.removeLike(filmId, userId);
        String message = "Пользователем (ID = " + userId + ") выполнено удаление лайка фильму (ID = " + filmId + ").";
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
    
    @ResponseBody
    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        List<Film> popularFilms = likeService.getPopularFilm(count);
        log.info("Выдан ответ на запрос о выдаче списка популярных фильмов.");
        return popularFilms;
    }
    
}
