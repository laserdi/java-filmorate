package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.storage.film.dao.GenreStorage;

import java.util.List;

@Service
@Qualifier("GenreDBService")
@Slf4j
public class GenreService {
    @Qualifier("GenreDBStorage")
    private final GenreStorage genreStorage;
    ValidationService validationService;
    
    public GenreService(GenreStorage genreStorage, ValidationService validationService) {
        this.genreStorage = genreStorage;
        this.validationService = validationService;
    }
    
    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
    
    public Genre getGenreById(Integer genreId) {
        validationService.checkExistGenreInDB(genreId);
        return genreStorage.getGenreById(genreId);
    }
}
