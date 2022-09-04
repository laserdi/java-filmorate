package ru.yandex.practicum.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    HashMap<Integer, User> users = new HashMap<>();
    
    /**
     * Получить список всех пользователей.
     *
     * @return список пользователей.
     */
    @Override
    public List<User> getAllUsersFromStorage() {
        return new ArrayList<>(users.values());
    }
    
    /**
     * Добавить пользователя в БД.
     *
     * @param user пользователь.
     * @return добавленный пользователь.
     */
    @Override
    public User addToStorage(User user) {
        users.put(user.getId(), user);
        return user;
    }
    
    
    /**
     * Обновить юзера в БД.
     *
     * @param user пользователь
     * @return обновлённый пользователь.
     */
    @Override
    public User updateInStorage(User user) {
        
        users.put(user.getId(), user);
        return users.get(user.getId());
    }
    
    /**
     * Удалить пользователя из БД.
     *
     * @param user пользователь
     * @return True - удалён. False - не выполнено.
     */
    @Override
    public User removeFromStorage(User user) {
        if (users.remove(user.getId(), user)) {
            return user;
        }
        return null;
    }
    
    /**
     * Получить пользователя по ID.
     *
     * @param id ID пользователя.
     * @return User - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    @Override
    public User getUserById(Integer id) {
        return users.getOrDefault(id, null);
    }
    
    /**
     * Получить пользователя по логину.
     *
     * @param login логин пользователя.
     * @return User - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    @Override
    public User getUserByLogin(String login) {
        return users.values().stream().filter(u -> u.getLogin().equals(login)).findFirst().orElse(null);
    }
    
    
    
}
