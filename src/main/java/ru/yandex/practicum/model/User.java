package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * целочисленный идентификатор — id;
 * <p>электронная почта — email;</p>
 * <p>логин пользователя — login;</p>
 * <p>имя для отображения — name;</p>
 * <p>дата рождения — birthday.</p>
 */
@Value
@Builder(toBuilder = true)
public class User {
    Integer id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    
    //<p>имя для отображения может быть пустым — в таком случае будет использован логин;</p>
    //User{id=-5, email='edd@hyhhg', login='login юзера №1', name='имя юзера 1', birthday=1983-12-20}
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (name == null || name.isEmpty() || name.isBlank()) {
            return "User{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", login='" + login + '\'' +
                    ", name='" + login + '\'' +
                    ", birthday=" + birthday +
                    '}';
        } else {
            return "User{" +
                    "id=" + id +
                    ", email='" + email + '\'' +
                    ", login='" + login + '\'' +
                    ", name='" + name + '\'' +
                    ", birthday=" + birthday +
                    '}';
            
        }
    }
}
