package ru.yandex.practicum.storage.like.dao;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface LikeStorage {
    
    /**
     * Пользователь удаляет лайк фильму.
     * @param filmId ID фильма.
     * @param userId ID юзера.
     */
    void removeLike(Integer filmId, Integer userId);
    /**
     * Пользователь снимает ранее поставленный лайк фильму.
     * Потом ставит новый.
     * @param filmId ID фильма.
     * @param userId ID юзера.
     */
    void addLike(Integer filmId, Integer userId);
    
    /**
     * Возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     * @param count число фильмов для результата.
     */
    public List<Film> getPopularFilm(Integer count);
}
