package ru.yandex.practicum.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.dao.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@Qualifier("InMemoryUserStorage")       //Используется для однозначности использования классов наследников интерфейса.
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    
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
    
    
    /**
     * Удалить пользователя из БД. True - удалено.
     *
     * @param id ID удаляемого пользователя.
     */
    @Override
    public void removeFromStorage(Integer id) {
        //Когда-нибудь надо будет сделать.
    }
    
    /**
     * Проверка наличия юзера в БД.
     *
     * @param id пользователя.
     * @return True - пользователь найден. False - пользователя нет в БД.
     */
    @Override
    public boolean isExistUserInDB(Integer id) {
        //Когда-нибудь надо будет сделать.
        return false;
    }
}
