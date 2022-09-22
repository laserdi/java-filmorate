package ru.yandex.practicum.storage.friends.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.friends.dao.FriendsStorage;

import java.util.List;

@Component("FriendsDBStorage")      //Используется для однозначности использования классов наследников интерфейса.
public class FriendsDBStorage implements FriendsStorage {
    /**
     * Добавить пользователей с ID1 и ID2 в друзья.
     *
     * @param id1 пользователь №1;
     * @param id2 пользователь №2.
     */
    @Override
    public void addEachOtherAsFriends(Integer id1, Integer id2) {
    
    }
    
    /**
     * Удалить пользователей из друзей.
     *
     * @param id1 пользователь №1.
     * @param id2 пользователь №2.
     */
    @Override
    public void deleteFromFriends(Integer id1, Integer id2) {
    
    }
    
    /**
     * Вывести список общих друзей.
     *
     * @param id1 пользователь №1
     * @param id2 пользователь №2
     * @return список общих друзей.
     */
    @Override
    public List<User> getCommonFriends(Integer id1, Integer id2) {
        return null;
    }
}
