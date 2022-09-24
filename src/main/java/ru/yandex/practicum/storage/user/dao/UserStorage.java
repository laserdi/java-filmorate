package ru.yandex.practicum.storage.user.dao;

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
     * @param id ID удаляемого пользователя.
     */
    void removeFromStorage(Integer id);
    
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
    
    /**
     * Проверка наличия юзера в БД.
     *
     * @param id пользователя.
     * @return True - пользователь найден. False - пользователя нет в БД.
     */
    boolean isExistUserInDB(Integer id);
    
}