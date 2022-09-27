package ru.yandex.practicum.storage.friends.dao;

import ru.yandex.practicum.model.User;

import java.util.List;

/**
 * Интерфейс работы с базой данных друзей.
 */
public interface FriendsStorage {
    
    /**
     * Вывести список друзей.
     * @return список ID друзей.
     */
    List<User> getFriends(Integer id);
    /**
     * Добавить пользователей с ID1 и ID2 в друзья.
     *
     * @param id пользователь №1;
     * @param idFriend пользователь №2.
     */
    void addFriend(Integer id, Integer idFriend, Integer idFriendship);
    
    /**
     * Удалить пользователей из друзей.
     *
     * @param id пользователь №1.
     * @param idFriend пользователь №2.
     */
    void deleteFromFriends(Integer id, Integer idFriend);
    
    /**
     * Вывести список общих друзей.
     *
     * @param id1 пользователь №1
     * @param id2 пользователь №2
     * @return список общих друзей.
     */
    public List<User> getCommonFriends(Integer id1, Integer id2);
    
    
    /**
     * Получить подписчиков пользователя.
     * @param id ID пользователя, которому надо их найти.
     * @return список фолловеров.
     */
    List<User> getFollower(Integer id);
    
    
    /**
     * Получить пользователей, на которых подписка.
     * @param id ID пользователя, которому надо их найти.
     * @return список пользователей, на которых подписан.
     */
    List<User> getSubscribers(Integer id);
}
