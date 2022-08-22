package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

/**
 * целочисленный идентификатор — id;
 * <p>электронная почта — email;</p>
 * <p>логин пользователя — login;</p>
 * <p>имя для отображения — name;</p>
 * <p>дата рождения — birthday.</p>
 */
@Data
@Builder(toBuilder = true)
//@NoArgsConstructor
//@AllArgsConstructor
public class User {
    static Integer count = 1;
    @Builder.Default
    Integer id = count;
    String email;
    String login;
    String name;
    LocalDate birthday;
    
    public static Integer getCount() {
        return count++;
    }
}
