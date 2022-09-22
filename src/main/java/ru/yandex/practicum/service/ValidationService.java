package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.dao.UserStorage;
import ru.yandex.practicum.storage.user.impl.UserDBStorage;

import java.time.LocalDate;
@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {
    @Qualifier("UserDBStorage")
    private final UserStorage userDBStorage;
    
    
    /**
     * Проверка удовлетворения полей объекта User требуемым параметрам:
     * <p>электронная почта не может быть пустой и должна содержать символ @;</p>
     * <p>логин не может быть пустым и содержать пробелы;</p>
     * <p>имя для отображения может быть пустым — в таком случае будет использован логин;</p>
     * <p>дата рождения не может быть в будущем.</p>
     * <p>Если 'name' пользователя отсутствует, то ему присваивается значение 'login'.</p>
     * @param user пользователь, которого необходимо проверить.
     * @throws ValidateException в объекте пользователя есть ошибки.
     */
    void checkUser(User user) throws ValidateException {
        Integer id = user.getId();
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();
        
        if (id == null) log.info("checkUser(): ID пользователя = null.");
        
        //электронная почта не может быть пустой и должна содержать символ @;
        if (email == null || email.isEmpty() || email.isBlank()) {
            log.info("checkUser(): Не пройдена проверка 'пустоты' адреса электронной почты.");
            throw new ValidateException("Адрес электронной почты не может быть пустым.");
        }
        if (!email.contains("@")) {
            log.info("checkUser(): Не пройдена проверка наличия '@' в адресе электронной почты.");
            throw new ValidateException("Адрес электронной почты должен содержать символ '@'.");
        }
        
        //логин не может быть пустым и содержать пробелы;
        if (login == null || login.isEmpty() || login.isBlank()) {
            log.info("checkUser(): Не пройдена проверка наличия логина.");
            throw new ValidateException("Отсутствует логин (" + login + ") пользователя.");
        }
        if (login.contains(" ")) {
            log.info("checkUser(): Не пройдена проверка отсутствия пробелов в логине.");
            throw new ValidateException("В логине не должно быть пробелов.");
        }
        
        //дата рождения не может быть в будущем
        if (birthday != null) {
            if (birthday.isAfter(LocalDate.now())) {
                log.info("checkUser(): Не пройдена проверка корректной даты рождения. Дата рождения ещё не наступила");
                throw new ValidateException("checkUser(): Дата рождения ещё не наступила. " +
                        "Введите корректную дату рождения.");
            }
        }
        
        nameSetAsLogin(user);
    }
    
    
    
    /**
     * Метод проверки наличия пользователя в базе данных по логину.
     *
     * @param user пользователь, наличие логина которого необходимо проверить в базе данных.
     * @return ID, найденный в БД по логину.
     * Если возвращается не null, то после этой проверки можно обновлять пользователя,
     * присвоив ему ID из базы данных.
     * <p>null - пользователя нет в базе данных.</p>
     */
    private Integer idFromDBByLogin(User user) {
        String login = user.getLogin();
        return userDBStorage.getAllUsersFromStorage().stream().filter(u -> u.getLogin().equals(login))
                .findFirst().map(User::getId)
                .orElse(null);
    }
    
    /**
     * Метод присвоения имени пользователя при его отсутствии.
     * Если имя пустое, то оно равно логину.
     *
     * @param user обрабатываемый пользователь.
     */
    private void nameSetAsLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имени пользователя присвоено значение логина = {}", user.getLogin());
        }
    }
    
    
    /**
     * Проверка наличия юзера в БД.
     * @param id пользователя.
     * @throws NotFoundRecordInBD - пользователь найден в БД.
     */
    public void checkExistInDB(Integer id) {
        if (userDBStorage.isExistInDB(id)) {
            return;
        }
        String error = "404. Пользователь с ID = '" + id + "' не найден в БД.";
        log.error(error);
        throw new NotFoundRecordInBD(error);
    }
    
    
}
