package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.dao.GenreStorage;
import ru.yandex.practicum.storage.like.dao.LikeStorage;

import java.util.HashSet;
import java.util.List;

@Service
@Qualifier("LikeDBService")
@Slf4j
public class LikeService {
    
    @Qualifier("LikeDBStorage")
    private final LikeStorage likeStorage;
    
    @Qualifier("GenreDBStorage")
    private final GenreStorage genreStorage;
    
    private final ValidationService validationService;
    @Autowired
    public LikeService(LikeStorage likeStorage, GenreStorage genreStorage, ValidationService validationService) {
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.validationService = validationService;
    }
    
    /**
     * Пользователь снимает ранее поставленный лайк фильму. Потом ставит новый.
     *
     * @param filmId ID фильма.
     * @param userId ID юзера.
     */
    public void addLike(Integer filmId, Integer userId) {
        likeStorage.addLike(filmId, userId);
    }
    
    /**
     * Пользователь удаляет лайк фильму.
     *
     * @param filmId ID фильма.
     * @param userId ID юзера.
     */
    public void removeLike(Integer filmId, Integer userId) {
        validationService.checkExistUserInDB(userId);
        validationService.checkExistFilmInDB(filmId);
        likeStorage.removeLike(filmId, userId);
    }
    
    /**
     * Возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, верните первые 10.
     *
     * @param count число фильмов для результата.
     */
    // TODO: 2022.09.25 06:34:09 Проверить ошибку !!!!!!!!!!!!!!!!!!!!!!!!! - @Dmitriy_Gaju
    public List<Film> getPopularFilm(Integer count) {
        List<Film> result =  likeStorage.getPopularFilm(count);
        for (Film film : result) {
            // TODO: 2022.09.25 22:09:55 Я сам понял свою ошибку. Здесь надо было использовать метод сервиса,
            //  а не прямой доступ к БД. В будущем переделаю.
            //  Там у меня путаница с возвращаемыми объектами: в одном - List, в другом Set
            //  Очень мало времени на исправление. Прошу отнестись снисходительно. - @Dmitriy_Gaju
            film.setGenres(new HashSet<>(genreStorage.findGenresOfFilmId(film.getId())));
        }
        return result;
    }
}
