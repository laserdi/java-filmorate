package ru.yandex.practicum.storage.friends.dao;

import ru.yandex.practicum.model.User;

import java.util.List;

public interface FriendsStorage {
    
    /**
     * Добавить пользователей с ID1 и ID2 в друзья.
     *
     * @param id1 пользователь №1;
     * @param id2 пользователь №2.
     */
    void addEachOtherAsFriends(Integer id1, Integer id2);
    
    /**
     * Удалить пользователей из друзей.
     *
     * @param id1 пользователь №1.
     * @param id2 пользователь №2.
     */
    void deleteFromFriends(Integer id1, Integer id2);
    
    /**
     * Вывести список общих друзей.
     *
     * @param id1 пользователь №1
     * @param id2 пользователь №2
     * @return список общих друзей.
     */
    public List<User> getCommonFriends(Integer id1, Integer id2);
    
}
