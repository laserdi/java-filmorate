package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

/**
 * целочисленный идентификатор — id;
 * <p>электронная почта — email;</p>
 * <p>логин пользователя — login;</p>
 * <p>имя для отображения — name;</p>
 * <p>дата рождения — birthday.</p>
 */
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @JsonIgnore
    static Integer count = 1;
    
    Integer id;
    
    @Email
    @NotBlank
    String email;
    
    @NotBlank
    String login;
    
    String name;
    
    @Past
    LocalDate birthday;
    
    public static Integer getCount() {
        return count++;
    }
}
