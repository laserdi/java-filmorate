package ru.yandex.practicum.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserMapper implements RowMapper<User> {
    
    /**
     * Метод конвертации объекта, полученного из БД 'users', в объект программы Java.
     * <p>ID — email — login — name — birthday</p>
     * <p>Здесь ещё не хватает списка друзей для полноценного объекта.</p>
     * @param rs (ResultSet) набор строк с данными.
     * @param rowNum номер строки.
     * @return преобразованный пользователь из БД в Java.
     * @throws SQLException возможное исключение.
     */
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("user_name"));
        user.setBirthday((rs.getTimestamp("birthday")).toLocalDateTime().toLocalDate());
        return user;
    }
}
