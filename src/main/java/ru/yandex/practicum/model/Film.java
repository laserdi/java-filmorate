package ru.yandex.practicum.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;


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
    
    public static Integer getCount() {
        return count++;
    }
}
