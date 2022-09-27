package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.service.GenreService;

import java.util.List;

@Slf4j
@Component
@RestController
public class GenreController {
    @Qualifier("GenreDBService")
    private final GenreService genreService;
    
    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }
    
    @GetMapping("/genres")
    public List<Genre> getAllGenres(){
        return genreService.getAllGenres();
    }
    
    @GetMapping("/genres/{id}")
    public Genre getGenre (@PathVariable("id") Integer genreID){
        return  genreService.getGenreById(genreID);
    }
    
}
