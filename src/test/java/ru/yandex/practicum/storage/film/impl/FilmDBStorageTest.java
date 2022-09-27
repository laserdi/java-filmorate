package ru.yandex.practicum.storage.film.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmDBStorageTest {
    FilmDBStorage filmDBStorage;
    @BeforeEach
    void setUp() {
//        filmDBStorage = new FilmDBStorage();
    }
    
    @Test
    void addInStorage() {
        //Genre genre = new Genre().toBuilder().build();
        //Set<Genre> genres = new HashSet<>(Arrays.asList(1,2,3,4));
        
        Film film = new Film().toBuilder().name("name Film 1").description("desc Film 1").duration(40)
                .mpa(new Mpa().toBuilder().name("mpa1").description("desc mpa1").build()).releaseDate(LocalDate.of(2001,1,1))
                .build();
/*
        String sqlQuery = "insert into FILMS " +
                "(FILM_NAME, FILM_DESC, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
*/
//        filmDBStorage.addInStorage(film);
        
    }
    
    @Test
    void updateInStorage() {
    }
    
    @Test
    void removeFromStorage() {
    }
    
    @Test
    void getAllFilms() {
    }
    
    @Test
    void getFilmById() {
    }
    
    @Test
    void getFilmByName() {
    }
}