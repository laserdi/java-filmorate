package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.dao.FilmStorage;
import ru.yandex.practicum.storage.film.dao.GenreStorage;
import ru.yandex.practicum.storage.film.dao.MpaStorage;
import ru.yandex.practicum.storage.user.dao.UserStorage;

@Service
@Slf4j
//@RequiredArgsConstructor
public class FilmService {
    @Qualifier("FilmDBStorage")
    private final FilmStorage filmDBStorage;
    
    @Qualifier("UserDBStorage")     //Используется для однозначности использования классов наследников интерфейса.
    private final UserStorage userDBStorage;
    
    @Qualifier("GenreDBStorage")
    private final GenreStorage genreDBStorage;
    
    @Qualifier("MpaDBStorage")
    private final MpaStorage mpaDBStorage;

    private final ValidationService validationService;
    
    @Autowired
    public FilmService(FilmStorage filmDBStorage, UserStorage userDBStorage, ValidationService validationService,
                       GenreStorage genreDBStorage, MpaStorage mpaDBStorage) {
        this.filmDBStorage = filmDBStorage;
        this.userDBStorage = userDBStorage;
        this.validationService = validationService;
        this.genreDBStorage = genreDBStorage;
        this.mpaDBStorage = mpaDBStorage;
    }
    public Film add(Film film) {
        validationService.checkExistFilmInDB(film.getId());
        filmDBStorage.addInStorage(film);
        genreDBStorage.addInDBFilm_Genre(film.getId(), film.getGenres());
        return film;
    }
}
