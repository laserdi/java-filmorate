package ru.yandex.practicum.storage.friends.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.friends.dao.FriendsStorage;
import ru.yandex.practicum.storage.mapper.UserMapper;

import java.util.List;

@Slf4j
@Repository("FriendsDBStorage")      //Используется для однозначности использования классов наследников интерфейса.
@RequiredArgsConstructor
public class FriendsDBStorage implements FriendsStorage {
    
    JdbcTemplate jdbcTemplate;
    
    UserMapper userMapper;
    
    @Autowired
    public FriendsDBStorage(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }
    
    
    
    /**
     * Добавить пользователя с idFriend в друзья к пользователю id.
     *
     * @param id       пользователь, который добавляет себе друга.
     * @param idFriend пользователь, которого добавляют в друзья.
     * @param idFriendship статус дружбы.
     */
    @Override
    public void addFriend(Integer id, Integer idFriend, Integer idFriendship) {
        String sqlQuery =
                "merge into IDS_FRIENDS (user_id, friend_id, FRIENDSHIP_ID) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, id, idFriend, idFriendship);
    }
    
    /**
     * Вывести список друзей.
     *
     * @param id ID юзера, друзей которого надо найти.
     * @return список ID друзей.
     */
    @Override
    public List<User> getFriends(Integer id) {
    
        final String sql = "select * from USERS where USER_ID in (select FRIEND_ID from IDS_FRIENDS where USER_ID = ?)";
        return jdbcTemplate.query(sql, userMapper, id);
    }
    
    
    /**
     * Удалить пользователя с ID = idFriend из друзей.
     *
     * @param id пользователь №1.
     * @param idFriend пользователь №2.
     */
    @Override
    public void deleteFromFriends(Integer id, Integer idFriend) {
        String sqlQueryForDeleteFriend = "delete from IDS_FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        log.debug("Пользователем с ID = {} запросил удаление из друзей пользователя с ID = {}.", id, idFriend);
        jdbcTemplate.update(sqlQueryForDeleteFriend, id, idFriend);
        log.debug("Пользователем с ID = {} выполнено удаление из друзей пользователя с ID = {}.", id, idFriend);
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
        String sql = "select * from USERS as U where USER_ID in " +
                "(select FRIEND_ID from IDS_FRIENDS as IDF where IDF.USER_ID = ? and IDF.FRIEND_ID IN " +
                "(select FRIEND_ID from IDS_FRIENDS where USER_ID = ?))";
        List<User> result;
        result = jdbcTemplate.query(sql, userMapper, id1, id2);
        System.out.printf(result.toString());
        return result;
    }
    
    /**
     * Получить подписчиков пользователя.
     *
     * @param id ID пользователя, которому надо их найти.
     * @return список фолловеров.
     */
    @Override
    public List<User> getFollower(Integer id) {
        //Когда-нибудь сделаю.
        return null;
    }
    
    /**
     * Получить пользователей, на которых подписка.
     *
     * @param id ID пользователя, которому надо их найти.
     * @return список пользователей, на которых подписан.
     */
    @Override
    public List<User> getSubscribers(Integer id) {
        //Когда-нибудь сделаю.
        return null;
    }
}
