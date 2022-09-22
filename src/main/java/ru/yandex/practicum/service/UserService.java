package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.dao.UserStorage;
import ru.yandex.practicum.storage.user.impl.UserDBStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    
    @Qualifier("UserDBStorage")     //Используется для однозначности использования классов наследников интерфейса.
    private final UserStorage userDBStorage;
    private final ValidationService validationService;
    
    
    @Autowired
    public UserService(UserStorage userDBStorage, ValidationService validationService) {
        this.userDBStorage = userDBStorage;
        this.validationService = validationService;
    }
    
    /**
     * Получить пользователя по ID.
     *
     * @param id ID пользователя.
     * @return User - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    public User getUserById(Integer id) {
        User result = userDBStorage.getUserById(id);
        if (result == null) {
            String error = "В БД отсутствует запись о пользователе при получении пользователя по ID = " + id + ".";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
        return result;
    }
    
    /**
     * Получение списка всех пользователей.
     *
     * @return Список пользователей.
     */
    public List<User> getAllUsers() {
        return userDBStorage.getAllUsersFromStorage();
    }
    
    
    /**
     * Добавить юзера в БД.
     *
     * @param user пользователь.
     * @return добавляемый пользователь.
     */
    public User addToStorage(User user) throws ValidateException, NotFoundRecordInBD {
        
        // TODO: 2022.09.21 02:20:26 Удалить старый UserServiceOld - @Dmitriy_Gaju
        //Проверяем необходимые поля, и, если имя пустое, то оно равно логину.
        validationService.checkUser(user);
        return userDBStorage.addToStorage(user);
    }
    
    /**
     * Обновить юзера в БД.
     *
     * @param user пользователь
     * @return обновлённый пользователь.
     */
    public User updateInStorage(User user) {
        //Проверяем необходимые поля, и, если имя пустое, то оно равно логину.
        validationService.checkUser(user);
        //Проверяем наличие записи о пользователе с ID = user.getId().
        validationService.checkExistInDB(user.getId());
        return userDBStorage.updateInStorage(user);
    }
    
    /**
     * Удалить пользователя из БД.
     *
     * @param id ID удаляемого пользователя
     * @throws NotFoundRecordInBD из метода validationService.checkExistInDB(id).
     */
    public void removeFromStorage(Integer id) {
        validationService.checkExistInDB(id);
        //Если пользователь есть в БД, то идём далее.
        userDBStorage.removeFromStorage(id);
        String error = "Выполнено удаление пользователя  из БД с ID = '" + id + ".";
        log.info(error);
    }
    
    
    /**
     * Добавить пользователей с ID1 и ID2 в друзья.
     *
     * @param id1 пользователь №1;
     * @param id2 пользователь №2.
     */
    public void addEachOtherAsFriends(Integer id1, Integer id2) {
        User friend1 = getUserById(id1);
        User friend2 = getUserById(id2);
        
        if (friend1 == null || friend2 == null) {
            String error = "При добавлении в друзья БД не найден(ы) пользователь(и). " +
                    "Проверьте передаваемые значения ID.";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
        friend1.getIdsFriends().add(id2);
        friend2.getIdsFriends().add(id1);
        
        addToStorage(friend1);
        addToStorage(friend2);
        log.info("Пользователь с ID = " + id1 + " подружился с пользователем с ID = " + id2 + ".");
    }
    
    /**
     * Удалить пользователей из друзей.
     *
     * @param id1 пользователь №1.
     * @param id2 пользователь №2.
     */
    public void deleteFromFriends(Integer id1, Integer id2) {
        User friend1 = getUserById(id1);
        User friend2 = getUserById(id2);
        
        if (friend1 == null || friend2 == null) {
            String error = "При удалении из друзей БД не найден(ы) пользователь(и). " +
                    "Проверьте передаваемые значения ID.";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
        if (!friend1.getIdsFriends().contains(id2) || !friend2.getIdsFriends().contains(id1)) {
            String error = "При удалении из друзей БД пользователь(и) не является(ются) " +
                    "другом (друзьями). Проверьте передаваемые значения ID.";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
        friend1.getIdsFriends().remove(id2);
        friend2.getIdsFriends().remove(id1);
        
        addToStorage(friend1);
        addToStorage(friend2);
        log.info("Дружба пользователя (ID = " + id1 + ") с пользователем (ID = " + id2 + ") завершена )-;");
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
     * Вывести список общих друзей.
     *
     * @param id1 пользователь №1
     * @param id2 пользователь №2
     * @return список общих друзей.
     */
    public List<User> getCommonFriends(Integer id1, Integer id2) {
        User friend1 = getUserById(id1);
        User friend2 = getUserById(id2);
        
        List<User> result = new ArrayList<>();
        if (friend1 == null || friend2 == null) {
            String error = "Одного из пользователей (а может, и двух) нет в БД. Проверьте их ID = "
                    + id1 + " и ID = " + id2 + ".";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
        
        if (friend1.getIdsFriends() == null || friend1.getIdsFriends().isEmpty()) {
            log.info("Список друзей пользователя с ID = " + id1 + "пуст.");
        } else if (friend2.getIdsFriends() == null || friend2.getIdsFriends().isEmpty()) {
            log.info("Список друзей пользователя с ID = " + id2 + "пуст.");
        } else {
            //ищем общих друзей.
            result = friend1.getIdsFriends().stream()
                    .filter(id -> friend2.getIdsFriends().contains(id))
                    .map(this::getUserById).collect(Collectors.toList());
        }
        return result;
    }
    
    /**
     * Вывести список друзей пользователя с ID.
     *
     * @param id ID пользователя.
     * @return список друзей.
     */
    public List<User> getUserFriends(Integer id) {
        User user = getUserById(id);
        if (user == null) {
            String error = "Запрошена выдача списка друзей пользователя с ID = " + id
                    + ", которого нет в БД.";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
        List<User> result = new ArrayList<>();
        for (Integer idFriend : user.getIdsFriends()) {
            User friend = getUserById(idFriend);
            if (friend == null) {
                String error = "При выдаче списка друзей пользователя в БД не найден друг с ID = " + idFriend + ".";
                log.error(error);
                throw new NotFoundRecordInBD(error);
            }
            result.add(getUserById(idFriend));
        }
        return result;
    }
    
    // TODO: 2022.09.04 17:37:19 В будущем удалить дублирующий метод. Ещё к тому же
    //  недоделанный и плохо названный. - @Dmitriy_Gaju
    
    /**
     * Метод проверки наличия пользователя в базе данных по ID.
     *
     * @param id пользователь, наличие логина которого необходимо проверить в базе данных.
     * @return ID, найденный в БД по логину.
     * Если возвращается не null, то после этой проверки можно обновлять пользователя,
     * присвоив ему ID из базы данных.
     * <p>null - пользователя нет в базе данных.</p>
     */
    private Integer idFromDBByID(Integer id) {
        
        return userDBStorage.getAllUsersFromStorage().stream().filter(u -> u.getId().equals(id))
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
        }
    }
    
    /**
     * Метод присвоения юзеру уникального ID, если ID не задан.
     *
     * @param user обрабатываемый пользователь.
     * @return True - уникальный ID присвоен.
     * <p>False - уникальный ID у пользователя user был.</p>
     */
    private boolean setUniqueIdForUserFromCount(User user) {
        if (user.getId() != null) {
            log.info("Уникальный ID пользователю не нужен. Изначальный ID = " + user.getId());
            return false;
        }
        while (true) {
            Integer count = User.getCount();
            if (userDBStorage.getUserById(count) == null) {
                user.setId(count);
                log.info("Уникальный ID фильму присвоен. ID = " + user.getId());
                return true;
            }
        }
    }
}
