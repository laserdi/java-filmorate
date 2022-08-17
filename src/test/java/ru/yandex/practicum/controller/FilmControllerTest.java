package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.ValidateException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController = new FilmController();
    
    /**
     * Добрый день.
     * Я понял свой недочёт в плане того, что можно было использовать метод создания-изменения
     * объектов Film с помощью функции film.toBuilder().setId(8).build().
     * Это я учёл при написании тестов для UserController.
     */
    
    //название не может быть пустым;
    @Test
    public void createdFilmWithFailName() {
        Film filmNameIsBlank = Film.builder().id(0).name("    ").description("имя фильма состоит из пробелов")
                .releaseDate(LocalDate.of(2000, 1, 4)).duration(100)
                .build();
        Film filmNameIsNull = Film.builder().id(1).name(null).description("имя фильма = null")
                .releaseDate(LocalDate.of(2000, 2, 5)).duration(100)
                .build();
        Film filmNameIsEmpty = Film.builder().id(2).name("").description("имя фильма пустое")
                .releaseDate(LocalDate.of(2000, 3, 6)).duration(200)
                .build();
        Film filmNameIsCorrectly = Film.builder().id(3).name("имя фильма").description("описание")
                .releaseDate(LocalDate.of(2000, 4, 7)).duration(10)
                .build();
        
        assertThrows(ValidateException.class, () -> filmController.checkFilm(filmNameIsBlank)
                , "Ошибка тестирования проверки фильма с 'name', состоящим из пробелов.");
        
        assertThrows(ValidateException.class, () -> filmController.checkFilm(filmNameIsNull)
                , "Ошибка тестирования проверки фильма с 'name' = null.");
        
        assertThrows(ValidateException.class, () -> filmController.checkFilm(filmNameIsEmpty)
                , "Ошибка тестирования проверки фильма с пустым 'name'.");
        assertDoesNotThrow(() -> filmController.checkFilm(filmNameIsCorrectly)
                , "Ошибка тестирования проверки фильма с корректным именем 'name'.");
    }
    
    //максимальная длина описания — 200 символов
    @Test
    public void createdFilmWithFailDescription() {
        
        Film filmDescriptionIs200Symbols = Film.builder().id(0).name("description = 200 символов")
                .description("12345678901234567890123456789012345678901234567890" +
                        "12345678901234567890123456789012345678901234567890" +
                        "12345678901234567890123456789012345678901234567890" +
                        "12345678901234567890123456789012345678901234567890")
                .releaseDate(LocalDate.of(2000, 1, 4)).duration(100)
                .build();
        
        Film filmDescriptionIs201Symbols = Film.builder().id(1).name("description = 201 символов")
                .description("12345678901234567890123456789012345678901234567890" +
                        "12345678901234567890123456789012345678901234567890" +
                        "12345678901234567890123456789012345678901234567890" +
                        "123456789012345678901234567890123456789012345678901")
                .releaseDate(LocalDate.of(2000, 2, 5)).duration(100)
                .build();
        
        Film filmDescriptionIsNull = Film.builder().id(2).name("описание фильма = null").description(null)
                .releaseDate(LocalDate.of(2000, 3, 6)).duration(100)
                .build();
        
        Film filmDescriptionIsNormal = Film.builder().id(3).name("фильм с корректным описанием")
                .description("корректное описание фильма")
                .releaseDate(LocalDate.of(2000, 4, 7)).duration(20)
                .build();
        
        assertDoesNotThrow(() -> filmController.checkFilm(filmDescriptionIsNull)
                , "Ошибка тестирования проверки фильма с 'description' = null.");
        assertDoesNotThrow(() -> filmController.checkFilm(filmDescriptionIs200Symbols)
                , "Ошибка тестирования проверки фильма с 'description', состоящим из 200 символов.");
        assertThrows(ValidateException.class, () -> filmController.checkFilm(filmDescriptionIs201Symbols)
                , "Ошибка тестирования проверки фильма с 'description', состоящим из 201 символа.");
        assertDoesNotThrow(() -> filmController.checkFilm(filmDescriptionIsNormal)
                , "Ошибка тестирования проверки фильма с корректным 'description'.");
    }
    
    //дата релиза — не раньше 28 декабря 1895 года;
    @Test
    public void createdFilmWithFailReleaseDate() {
        Film filmWithReleaseDate1895_12_27 = Film.builder().id(0).name("имя фильма с 'плохой' датой")
                .description("описание фильма с датой до 1895.12.28")
                .releaseDate(LocalDate.of(1895, 12, 27)).duration(5)
                .build();
        Film filmWithReleaseDate1895_12_28 = Film.builder().id(1).name("имя фильма с пограничной датой")
                .description("описание").releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100).build();
        Film filmWithReleaseDate1895_12_29 = Film.builder().id(2).name("имя фильма с пограничной датой справа")
                .description("описание").releaseDate(LocalDate.of(1895, 12, 29))
                .duration(100).build();
        
        assertThrows(ValidateException.class, () -> filmController.checkFilm(filmWithReleaseDate1895_12_27)
                , "Ошибка тестирования проверки фильма с датой релиза ранее 28 декабря 1895 года.");
        assertDoesNotThrow(() -> filmController.checkFilm(filmWithReleaseDate1895_12_28)
                , "Ошибка тестирования проверки фильма с датой релиза 28 декабря 1895 года.");
        assertDoesNotThrow(() -> filmController.checkFilm(filmWithReleaseDate1895_12_29)
                , "Ошибка тестирования проверки фильма с датой релиза после 28 декабря 1895 года.");
    }
    
    //продолжительность фильма должна быть положительной
    @Test
    public void createdFilmWithFailDuration() {
        Film filmWithNegativeDuration = Film.builder().id(0).name("name")
                .description("описание фильма с отрицательной продолжительностью")
                .releaseDate(LocalDate.of(2000, 1, 4)).duration(-1).build();
        Film filmWithZeroOfDuration = Film.builder().id(1).name("имя").description("описание")
                .releaseDate(LocalDate.of(2000, 2, 5)).duration(0).build();
        Film filmWithDuration_1 = Film.builder().id(3).name("имя").description("описание")
                .releaseDate(LocalDate.of(2000, 3, 6)).duration(1).build();
        Film filmWithCorrectlyDuration = Film.builder().id(3).name("имя").description("описание")
                .releaseDate(LocalDate.of(2000, 3, 6)).duration(10).build();
        
        assertThrows(ValidateException.class, () -> filmController.checkFilm(filmWithNegativeDuration)
                , "Ошибка тестирования проверки фильма с отрицательной продолжительностью = -1.");
        assertThrows(ValidateException.class, () -> filmController.checkFilm(filmWithZeroOfDuration)
                , "Ошибка тестирования проверки фильма с продолжительностью = 0.");
        assertDoesNotThrow(()-> filmController.checkFilm(filmWithCorrectlyDuration)
                ,"Ошибка тестирования проверки фильма с корректной продолжительностью = 10");
        
    }
}