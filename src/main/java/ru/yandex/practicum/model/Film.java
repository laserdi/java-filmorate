package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * целочисленный идентификатор — id;
 * название — name;
 * описание — description;
 * дата релиза — releaseDate;
 * продолжительность фильма — duration.
 */
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
@Data
@Builder
public class Film {
    Integer id;
    String name;
    String description;
    LocalDate releaseDate;
    /**
     * Продолжительность фильма в минутах.
     */
    Integer duration;
}
