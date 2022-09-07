package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    UserStorage inMemoryUStorage;
    
    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUStorage = inMemoryUserStorage;
    }
    
    /**
     * Получить пользователя по ID.
     *
     * @param id ID пользователя.
     * @return User - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    public User getUserById(Integer id) {
        User result = inMemoryUStorage.getUserById(id);
        if (result == null) {
            String error = "В БД отсутствует запись о пользователе при получении пользователя по ID = " + id + ".";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
        return inMemoryUStorage.getUserById(id);
    }
    
    /**
     * Получение списка всех пользователей.
     *
     * @return Список пользователей.
     */
    public List<User> getAllUsers() {
        return inMemoryUStorage.getAllUsersFromStorage();
    }
    
    
    /**
     * Добавить юзера в БД.
     *
     * @param user пользователь.
     * @return добавляемый пользователь.
     */
    public User addToStorage(User user) throws ValidateException, NotFoundRecordInBD {
        
        //Проверяем необходимые поля.
        checkUser(user);
        //Если имя пустое, то оно равно логину.
        nameSetAsLogin(user);
        
        //ID, найденный в БД по логину:
        Integer idFromDB = idFromDBByLogin(user);
        
        if (idFromDB != null) {
            
            if (user.getId() == null || user.getId().equals(idFromDB)) {
                //Проверяем ID входящего пользователя. Если он null или равен ID, найденному в БД по логину:
                //будет обновление существующего пользователя.
                user.setId(idFromDB);       //операция необходима при user.getId() == null.
                String message = "В результате добавления пользователя в БД обновлён существующий пользователь " +
                        "с логином = '" + user.getLogin() + "'." + user;
                log.info(message);
                return inMemoryUStorage.updateInStorage(user);
            } else {
                //Если ID входящего пользователя не null и не равен ID из БД,
                // значит ошибка в запросе, поскольку,
                // возможно, требуется обновление существующего пользователя.
                String error = "Ошибка добавления пользователя в БД. ID = '" + user.getId()
                        + "' входящего пользователя с логином '" + user.getLogin()
                        + "' не равен ID = '" + idFromDB + "' из БД пользователей. ";
                log.error(error);
                throw new NotFoundRecordInBD(error);
            }
            
        }
        
        //Получается, что пришёл "новый" пользователь, логина которого нет в БД.
        //И, если с пустым ID, то, сразу же его заполним.
        setUniqueIdForUserFromCount(user);
        
        String message = "В результате добавления пользователя в БД добавлен новый пользователь:\t" + user;
        log.info(message);
        return inMemoryUStorage.addToStorage(user);
        
    }
    
    /**
     * Обновить юзера в БД.
     *
     * @param user пользователь
     * @return обновлённый пользователь.
     */
    public User updateInStorage(User user) {
        //Проверяем необходимые поля.
        checkUser(user);
        
        User userTemp = user.toBuilder().build(); //Делаем копию юзера для дальнейшей обработки.
        nameSetAsLogin(userTemp);
        Integer idFromDB = idFromDBByLogin(userTemp);
        if (idFromDB != null) {
            //********** Пользователь по логину присутствует в БД.
            if (userTemp.getId() == null || userTemp.getId().equals(idFromDB)) {
                //ID входящего пользователя равен null или ID, найденному из БД по логину.
                //Присваиваем входящему пользователю ID из БД.
                userTemp.setId(idFromDB); //данная операция нужна только при ID входящего пользователя == null
                String message = "Выполнено обновление существующей записи о пользователе с логином: '"
                        + userTemp.getLogin() + "'.";
                log.info(message);
                return inMemoryUStorage.updateInStorage(userTemp);
            } else {
                String error = "Login пользователя есть в БД, но ID в БД (" + idFromDB
                        + ") отличается от принимаемого '" + userTemp.getId() + "'.";
                log.error(error);
                throw new NotFoundRecordInBD(error);
            }
        }
        
        //********** Пользователь по логину отсутствует в БД.******************
        if (userTemp.getId() == null) {
            //Генерируем для входящего пользователя ID и добавляем нового пользователя в БД.
            userTemp.setId(User.getCount());
            
            String message = "В результате обновления пользователя в БД добавлен новый пользователь:\t" + user;
            log.info(message);
            return inMemoryUStorage.addToStorage(userTemp);
        } else if (inMemoryUStorage.getUserById(userTemp.getId()) != null) {
            // иначе если ID пользователя есть в БД {
            //      обновление логина пользователя -> обновляем запись в БД.
            //старый логин:
            String oldLogin = inMemoryUStorage.getUserById(userTemp.getId()).getLogin();
            String newLogin = userTemp.getLogin();
            log.info("Выполнено обновление учётной записи с изменением логина. " +
                    "Был логин = '" + oldLogin + "', а стал - '" + newLogin + "'.");
            return inMemoryUStorage.updateInStorage(userTemp);
        } else {
            //ID и Логина нет в БД. -> Не верный запрос.
            String error = "В запросе на обновление пользователя ошибка. Login '" + userTemp.getLogin()
                    + "'и ID = '" + userTemp.getId() + "' не найдены в БД.";
            log.error(error);
            throw new NotFoundRecordInBD(error);
        }
    }
    
    /**
     * Удалить пользователя из БД.
     *
     * @param user пользователь
     * @return True - удалён. False - не выполнено.
     */
    public User removeFromStorage(User user) {
        User deletedUser = inMemoryUStorage.removeFromStorage(user);
        if (deletedUser == null) {
            String error = "При удалении пользователя из БД отсутствует запись об удаляемом пользователе.";
            log.error(error + user.toString());
            throw new NotFoundRecordInBD(error + "Проверьте передаваемые данные.");
        }
        return deletedUser;
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
     * Проверка удовлетворения полей объекта User требуемым параметрам:
     * <p>электронная почта не может быть пустой и должна содержать символ @;</p>
     * <p>логин не может быть пустым и содержать пробелы;</p>
     * <p>имя для отображения может быть пустым — в таком случае будет использован логин;</p>
     * <p>дата рождения не может быть в будущем.</p>
     *
     * @param user пользователь, которого необходимо проверить.
     * @throws ValidateException в объекте пользователя есть ошибки.
     */
    public void checkUser(User user) throws ValidateException {
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
        return inMemoryUStorage.getAllUsersFromStorage().stream().filter(u -> u.getLogin().equals(login))
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
    
    // TODO: 2022.09.04 17:37:19 Удалить дублирующий метод. Ещё к тому же
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
        
        return inMemoryUStorage.getAllUsersFromStorage().stream().filter(u -> u.getId().equals(id))
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
            if (inMemoryUStorage.getUserById(count) == null) {
                user.setId(count);
                log.info("Уникальный ID фильму присвоен. ID = " + user.getId());
                return true;
            }
        }
    }
}
