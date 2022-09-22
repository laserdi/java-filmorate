package ru.yandex.practicum.storage.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.mapper.UserMapper;
import ru.yandex.practicum.storage.user.dao.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
@Qualifier("UserDBStorage")         //Используется для однозначности использования классов наследников интерфейса.
public class UserDBStorage implements UserStorage {
    
    
    private JdbcTemplate jdbcTemplate;
    private UserMapper userMapper;
    
    @Autowired
    public UserDBStorage(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }
    
    /**
     * Добавить юзера в БД.
     *
     * @param user пользователь.
     * @return добавляемый пользователь.
     */
    @Override
    public User addToStorage(User user) {
        String sqlQuery = "insert into USERS(EMAIL, LOGIN, USER_NAME,BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
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
        String sqlQuery = "update USERS set EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? " +
                "where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday()
                , user.getId());
        return user;
    }
    
    /**
     * Удалить пользователя из БД.
     *
     * @param id ID удаляемого пользователя.
     */
    @Override
    public void removeFromStorage(Integer id) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
    
    /**
     * Получить список всех пользователей.
     *
     * @return список пользователей.
     */
    @Override
    public List<User> getAllUsersFromStorage() {
        String sqlQuery = "select * from USERS";
        log.debug("Попытка получить всех пользователей из БД.");
        return jdbcTemplate.query(sqlQuery, userMapper);
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
        String sqlQuery = "select * from USERS where USER_ID = ?";
        log.debug("Попытка получить пользователя с id {} из БД.", id);
        List<User> result = jdbcTemplate.query(sqlQuery, userMapper, id);
        log.debug("Вывод на экран результата запроса юзера по id = {} — {}", id, result);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
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
        String sqlQuery = "select * from USERS where LOGIN = ?";
        log.debug("Попытка получить пользователя с логином {} из БД.", login);
        User result = jdbcTemplate.queryForObject(sqlQuery, userMapper, login);
        
        System.out.printf("Проверка результата запроса юзера по логину: " + result + "\n");
        return result;
    }
    
    /**
     * Проверка наличия юзера в БД.
     * @param id пользователя.
     * @return True - пользователь найден. False - пользователя нет в БД.
     */
    @Override
    public boolean isExistInDB(Integer id) {
        String sqlQuery = "select COUNT(*) from USERS where USER_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        if (count != null) {
            return count.equals(1);
        }
        return false;
    }
    
}
