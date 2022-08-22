package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

/**
 *
 * Добрый день, Наталья.
 *
 * Наталья: "а почему закомментировал их? я согласна с тобой, лучше использовать эти аннотации, чем общую Data."
 * Мой ответ: "Это себе оставил, как напоминание."
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    Integer id;
    String name;
    String description;
    LocalDate releaseDate;
    /**
     * Продолжительность фильма в минутах.
     */
    Integer duration;
/**
 *
 * Наталья:
 * не хочешь попробовать выполнить задание и добавить аннотации валидации к полям? вообще этой штукой пользуются всегда во всех рабочих проектах и тебе необходимо просто добавить зависимость.
 * это для обеих моделей данных
 * попробуй добавить аннотации валидации к полям например какие нибудь из таких
 * @notblank
 * @SiZe(min = 1, max = 200)
 * @NotNull
 * @min(1)
 * @Email
 *
 * Мой ответ: "Спасибо за совет, но пока нет времени разобраться с этими аннотациями."
 */

}
