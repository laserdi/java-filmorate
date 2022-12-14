package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.model.User;

import java.util.List;

public interface UserStorage {
    
    /**
     * Добавить юзера в БД.
     *
     * @param user пользователь.
     * @return добавляемый пользователь.
     */
    User addToStorage(User user);
    
    /**
     * Обновить юзера в БД.
     *
     * @param user пользователь
     * @return обновлённый пользователь.
     */
    User updateInStorage(User user);
    
    /**
     * Удалить пользователя из БД. True - удалено.
     *
     * @param user пользователь
     * @return True - удалён. False - не выполнено.
     */
    User removeFromStorage(User user);
    
    /**
     * Получить список всех пользователей.
     *
     * @return список пользователей.
     */
    List<User> getAllUsersFromStorage();
    
    /**
     * Получить пользователя по ID.
     *
     * @param id ID пользователя.
     * @return User - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    User getUserById(Integer id);
    
    /**
     * Получить пользователя по логину.
     *
     * @param login логин пользователя.
     * @return User - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    User getUserByLogin(String login);
}
