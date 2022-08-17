package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FilmoRateApplicationTest {
    
    @Test
    void shouldFalseFilmNameIsBlank() {
//        Film film = new Film(1, "", "description", LocalDate.of(2000, 1, 1), 100);
//        assertThrows(ValidationException.class, () -> controller.validatorFilm(film));
    }
}
