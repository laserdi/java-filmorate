package ru.yandex.practicum.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    Film filmIsCorrectly;
    FilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage);
    List<Film> expectedFilmList;
    
    @BeforeEach
    void setUp() {
        filmIsCorrectly = Film.builder().id(0).name("'Правильный' фильм").description("Описание правильного фильма.")
                .releaseDate(LocalDate.of(2012, 12, 20)).duration(51).build();
        expectedFilmList = new ArrayList<>();
    }
    
    @AfterEach
    void tearDown() {
    }
    
    @Test
    void getAllFilms() {
        myAssertEqualsList(expectedFilmList, filmService.getAllFilms()
                , "Ошибка сравнения пустого списка фильмов.");
        
        expectedFilmList.add(filmIsCorrectly);
        filmService.createFilm(filmIsCorrectly);
        myAssertEqualsList(expectedFilmList, filmService.getAllFilms()
                , "Ошибка сравнения списка фильмов после добавления туда нормального фильма.");
    }
    
    //название не может быть пустым;
    @Test
    void createFilmWithFailName() {
        Film filmNameIsBlank = filmIsCorrectly.toBuilder().id(1)
                .name("    ").description("имя фильма состоит из пробелов").build();
        Film filmNameIsNull = filmIsCorrectly.toBuilder().id(2)
                .name(null).description("имя фильма = null").build();
        Film filmNameIsEmpty = filmIsCorrectly.toBuilder().id(3)
                .name("").description("имя фильма пустое").build();
        Film filmNameIsCorrectly = filmIsCorrectly.toBuilder().build();
        
        assertDoesNotThrow(() -> filmService.createFilm(filmIsCorrectly)
                , "Ошибка тестирования при корректном фильме.");
        
        assertThrows(ValidateException.class, () -> filmService.createFilm(filmNameIsNull)
                , "Ошибка тестирования при 'name' = null.");
        assertThrows(ValidateException.class, () -> filmService.createFilm(filmNameIsEmpty)
                , "Ошибка тестирования при пустом 'name'.");
        assertThrows(ValidateException.class, () -> filmService.createFilm(filmNameIsBlank)
                , "Ошибка тестирования при 'login', состоящем только из пробелов.");
        
        expectedFilmList.add(filmNameIsCorrectly);
        myAssertEqualsList(expectedFilmList, filmService.getAllFilms()
                , "Ошибка сравнения списка фильмов после добавления туда фильмов с 'хорошим' "
                        + "именем, и тремя плохими.");
    }
    
    //максимальная длина описания — 200 символов
    @Test
    public void createdFilmWithFailDescription() {
        
        String description200Symbols = "1234567890".repeat(20);
        Film filmDescriptionIs200Symbols = filmIsCorrectly.toBuilder().id(1).name("description = 200 символов")
                .description(description200Symbols).build();
        
        String description201Symbols = description200Symbols + "1";
        Film filmDescriptionIs201Symbols = filmIsCorrectly.toBuilder().id(2).name("description = 201 символ")
                .description(description201Symbols).build();
        
        Film filmDescriptionIsNull = filmIsCorrectly.toBuilder().id(3).name("описание фильма = null")
                .description(null).build();
        
        Film filmDescriptionIsNormal = filmIsCorrectly.toBuilder().build();
        
        assertDoesNotThrow(() -> filmService.createFilm(filmDescriptionIsNull)
                , "Ошибка тестирования создания записи о фильме с 'description' = null.");
        assertDoesNotThrow(() -> filmService.createFilm(filmDescriptionIs200Symbols)
                , "Ошибка тестирования создания записи о фильме с 'description', состоящим из 200 символов.");
        assertThrows(ValidateException.class, () -> filmService.createFilm(filmDescriptionIs201Symbols)
                , "Ошибка тестирования создания записи о фильме с 'description', состоящим из 201 символа.");
        assertDoesNotThrow(() -> filmService.createFilm(filmDescriptionIsNormal)
                , "Ошибка тестирования создания записи о фильме с корректным 'description'.");
        expectedFilmList.add(filmDescriptionIsNull);
        expectedFilmList.add(filmDescriptionIs200Symbols);
        expectedFilmList.add(filmDescriptionIsNormal);
        
        //сравниваем ожидаемый и актуальный списки фильмов
        myAssertEqualsList(expectedFilmList, filmService.getAllFilms()
                , "Ошибка сравнения списка фильмов после добавления туда фильмов с одним 'плохим'"
                        + "описанием, и тремя 'хорошими'.");
    }
    
    
    //дата релиза — не раньше 28 декабря 1895 года;
    @Test
    public void createdFilmWithFailReleaseDate() {
        Film filmWithReleaseDate1895_12_27 = filmIsCorrectly.toBuilder().id(1).name("имя фильма с 'плохой' датой")
                .releaseDate(LocalDate.of(1895, 12, 27)).duration(5).build();
        Film filmWithReleaseDate1895_12_28 = filmIsCorrectly.toBuilder().id(2).name("имя фильма с пограничной датой")
                .description("описание").releaseDate(LocalDate.of(1895, 12, 28)).build();
        Film filmWithReleaseDate1895_12_29 = filmIsCorrectly.toBuilder().id(3)
                .name("имя фильма с пограничной датой справа").description("описание")
                .releaseDate(LocalDate.of(1895, 12, 29)).build();
        
        assertThrows(ValidateException.class, () -> filmService.createFilm(filmWithReleaseDate1895_12_27)
                , "Ошибка тестирования проверки фильма с датой релиза ранее 28 декабря 1895 года.");
        assertDoesNotThrow(() -> filmService.createFilm(filmWithReleaseDate1895_12_28)
                , "Ошибка тестирования проверки фильма с датой релиза 28 декабря 1895 года.");
        assertDoesNotThrow(() -> filmService.createFilm(filmWithReleaseDate1895_12_29)
                , "Ошибка тестирования проверки фильма с датой релиза после 28 декабря 1895 года.");
        
        expectedFilmList.add(filmWithReleaseDate1895_12_28);
        expectedFilmList.add(filmWithReleaseDate1895_12_29);
        myAssertEqualsList(expectedFilmList, filmService.getAllFilms()
                , "Ошибка сравнения списка фильмов после добавления туда фильмов с одной 'плохой'"
                        + "датой, и двумя 'хорошими'.");
    }
    
    //продолжительность фильма должна быть положительной
    @Test
    public void createdFilmWithFailDuration() {
        Film filmWithNegativeDuration = filmIsCorrectly.toBuilder().id(1).duration(-1)
                .description("описание фильма с отрицательной продолжительностью").build();
        Film filmWithZeroOfDuration = filmIsCorrectly.toBuilder().id(2).duration(0)
                .description("описание фильма с продолжительностью = 0").build();
        Film filmWithDuration_1 = filmIsCorrectly.toBuilder().id(3).duration(1)
                .description("описание фильма с продолжительностью = 1").build();
        Film filmWithCorrectlyDuration = filmIsCorrectly.toBuilder().build();
        
        assertThrows(ValidateException.class, () -> filmService.createFilm(filmWithNegativeDuration)
                , "Ошибка тестирования проверки фильма с отрицательной продолжительностью = -1.");
        assertThrows(ValidateException.class, () -> filmService.createFilm(filmWithZeroOfDuration)
                , "Ошибка тестирования проверки фильма с продолжительностью = 0.");
        assertDoesNotThrow(() -> filmService.createFilm(filmWithDuration_1)
                , "Ошибка тестирования проверки фильма с корректной продолжительностью = 1.");
        assertDoesNotThrow(() -> filmService.createFilm(filmWithCorrectlyDuration)
                , "Ошибка тестирования проверки фильма с корректной продолжительностью.");
        
        expectedFilmList.add(filmWithDuration_1);
        expectedFilmList.add(filmWithCorrectlyDuration);
        myAssertEqualsList(expectedFilmList, filmService.getAllFilms()
                , "Ошибка сравнения списка фильмов после добавления туда фильмов с двумя 'плохими'"
                        + "и двумя 'хорошими' продолжительностями.");
    }
    
    //название не может быть пустым;
    @Test
    void updateFilmWithFailName() {
        assertThrows(NotFoundRecordInBD.class, () -> filmService.updateFilm(filmIsCorrectly)
                , "Ошибка тестирования при обновлении фильма, которого нет в БД.");
        myAssertEqualsList(expectedFilmList, filmService.getAllFilms()
                , "Ошибка тестирования при обновлении фильма, которого нет в БД.");
        //Создали фильм для его обновления.
        filmService.createFilm(filmIsCorrectly);
        
        Film filmNameIsBlank = filmIsCorrectly.toBuilder().id(1)
                .name("    ").description("имя фильма состоит из пробелов").build();
        Film filmNameIsNull = filmIsCorrectly.toBuilder().id(2)
                .name(null).description("имя фильма = null").build();
        Film filmNameIsEmpty = filmIsCorrectly.toBuilder().id(2)
                .name("").description("имя фильма пустое").build();
        Film filmNameIsCorrectly = filmIsCorrectly.toBuilder().name("фильм для обновления").build();
        
        assertDoesNotThrow(() -> filmService.updateFilm(filmNameIsCorrectly)
                , "Ошибка тестирования при корректном фильме.");
        
        assertThrows(ValidateException.class, () -> filmService.updateFilm(filmNameIsNull)
                , "Ошибка тестирования при 'name' = null.");
        assertThrows(ValidateException.class, () -> filmService.updateFilm(filmNameIsEmpty)
                , "Ошибка тестирования при пустом 'name'.");
        assertThrows(ValidateException.class, () -> filmService.updateFilm(filmNameIsBlank)
                , "Ошибка тестирования при 'login', состоящем только из пробелов.");
        
        expectedFilmList.add(filmNameIsCorrectly);
        myAssertEqualsList(expectedFilmList, filmService.getAllFilms()
                , "Ошибка сравнения списка фильмов после добавления туда фильмов с 'хорошим' "
                        + "именем, и тремя плохими.");
    }
    
    @Test
    void checkFilm() {
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
        
    }
    
    /**
     * Метод сравнения списков.
     *
     * @param expectedList ожидаемый список.
     * @param actuallyList актуальный список.
     * @param message      сообщение при ошибке сравнения списков.
     */
    private void myAssertEqualsList(List<Film> expectedList, List<Film> actuallyList, String message) {
        List<Film> differences = new ArrayList<>(expectedList);
        differences.removeAll(actuallyList);
        assertEquals(0, differences.size()
                , message);
    }
    
}