package ru.yandex.practicum.model;

import lombok.*;

/**
 * Класс, описывающий жанры фильмов.
 * Содержит   id = genre_id из таблицы genres в БД.
 * Содержит name = genre_name из таблицы genres в БД.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
//@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Genre {
    private Integer id;
    private String name;
}
