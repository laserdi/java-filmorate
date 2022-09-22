package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class Film {
    static Integer count = 1;
    
    @NotNull
    @NotBlank
    Integer id;
    @NotNull
    @NotBlank
    String name;
    
    //@Size(max = 200)
    String description;
    
    LocalDate releaseDate;
    /**
     * Продолжительность фильма в минутах.
     */
    @Positive
    Integer duration;
    
    @NotNull
    Mpa mpa;
    
    @JsonIgnore
    Set<Integer> likes = new HashSet<>();
    
    @JsonIgnore
    Set<Genre> genres = new HashSet<>();
    
    /**
     * Метод генерации счётчика.
     * @return целое число.
     */
    public static Integer getCount() {
        return count++;
    }
}
