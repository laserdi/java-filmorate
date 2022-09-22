package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    
    String email;
    
    String login;
    
    String name;
    
    LocalDate birthday;
    
    //ID друзей
    @JsonIgnore
    Set<Integer> idsFriends = new HashSet<>();
    
    public static Integer getCount() {
        return count++;
    }
    
    /**
     * Есть ли в списке друзей друг с ID = id?
     * @param id ID друга.
     * @return True - есть, False - нет.
     */
    public boolean containsFriend(Integer id) {
        return idsFriends.contains(id);
    }
    
    /**
     * Добавить ID друга в список друзей.
     * @param id ID друга.
     */
    public void addIdFriend(Integer id) {
        idsFriends.add(id);
    }
    /**
     * Удалить ID друга из списка друзей.
     * @param id ID друга.
     */
    public void removeIdFriend(Integer id) {
        idsFriends.remove(id);
    }
}
