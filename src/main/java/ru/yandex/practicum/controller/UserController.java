package ru.yandex.practicum.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.ValidateException;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Добавьте в классы-контроллеры эндпоинты с подходящим типом запроса для каждого из случаев.
 * <p>
 * Для UserController:
 * создание пользователя;
 * обновление пользователя;
 * получение списка всех пользователей.
 */


@Slf4j
@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    
    @GetMapping("/users")
    public List<User> findAllUsers() {
        log.info("Выдан ответ на запрос всех пользователей.");
        return new ArrayList<>(users.values());
    }
    
    /**
     * Создание пользователей.
     *
     * @param user из тела запроса.
     * @return созданный пользователь.
     */
    
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            
            checkUser(user);
            //заполняем сущность необходимыми данными
            
            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            
            //ID, найденный в БД по логину:
            Integer idFromDB = idUserLoginAlreadyExistInDB(user);
            if (idFromDB != null) {
                //Проверяем ID входящего пользователя. Если он есть, то ошибка запроса:
                if (user.getId() == null || user.getId().equals(idFromDB)) {
                    //Будет обновление существующего пользователя.
                    user.setId(idFromDB);
                    users.put(user.getId(), user);
                    System.out.println("В БД добавлен новый пользователь:\t" + user);
                    return ResponseEntity.ok(user);
                } else {
                    //Если ID входящего пользователя не null или не равен ID из БД,
                    // значит ошибка в запросе, поскольку,
                    // возможно, требуется обновление существующего пользователя.
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Проверьте ID пользователя с логином " + user.getLogin());
                }
                
            } else {
                //Получается, что пришёл "новый" пользователь, логина которого нет в БД.
                //И, если с пустым ID, то, сразу же его заполним.
                if (user.getId() == null) {
                    user.setId(User.getCount());
                }
                users.put(user.getId(), user);
                System.out.println("В БД добавлен новый пользователь:\t" + user);
                return ResponseEntity.ok(user);
            }
        } catch (ValidateException ex) {
            log.error("Ошибка добавления пользователя в базу данных. Ошибка: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Ошибка добавления/обновления пользователя в базе данных. Ошибка: "
                    + ex.getMessage());
        }
    }
    
    //Обновление пользователя.
    //Пользователь есть в БД по логину?
    //  да: {
    //  	ID не равен null или совпадает с ID из БД   {
    //  		если ID пользователя == null   {
    //            присваиваем ID из БД пользователю.
    //            обновляем запись в БД.
    //            Return Ok.
    //        } иначе {
    //  			если ID совпадает с ID из БД {
    //                обновляем запись в БД.
    //                Return Ok.
    //  			} иначе {
    //  				Bad Request 500, Not Found 404
    //  			}
    //        }
    //  }
    //  Нет: {
    //	    если нет ID {
    //          присваиваем сгенерированный ID;
    //          вносим в БД
    //          return OK.
    //      } иначе если ID есть в БД {
    //          обновление логина пользователя.
    //          обновляем запись в БД.
    //      } иначе если ID нет в БД {
    //		    Bad Request 500, Not Found 404
    //      }
    //}
    
    /**
     * Обновление информации о существующем пользователе.
     *
     * @param user обновляемый пользователь.
     * @return ответ о совершённом действии.
     */
    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            checkUser(user);
            
            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            User userForwork = user.toBuilder().build();
            Integer idFromDB = idUserLoginAlreadyExistInDB(userForwork);
            if (idFromDB != null) {
                //********** Пользователь по логину присутствует в БД.
                if (userForwork.getId() == null) {
                    //Присваиваем входящему пользователю ID из БД.
                    userForwork.setId(idFromDB);
                    users.put(userForwork.getId(), userForwork);
                    log.info("Выполнено обновление существующей записи о пользователе с логином: \""
                            + userForwork.getLogin() + "\".");
                    return ResponseEntity.status(HttpStatus.OK).body(userForwork);
                } else if (userForwork.getId().equals(idFromDB)) {
                    users.put(userForwork.getId(), userForwork);
                    log.info("Выполнено обновление существующей записи о пользователе с логином: \""
                            + userForwork.getLogin() + "\".");
                    return ResponseEntity.status(HttpStatus.OK).body(userForwork);
                } else {
                    log.error("Login пользователя есть в БД, но ID в БД (" + idFromDB
                            + ") отличается от принимаемого (" + userForwork.getId() + ").");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Login пользователя есть в БД, но ID в БД (" + idFromDB
                                    + ") отличается от принимаемого (" + userForwork.getId() + ").");
                }
            } else {
                //********** Пользователь по логину отсутствует в БД.
                if (userForwork.getId() == null) {
                    //Генерируем для входящего пользователя ID и добавляем нового пользователя в БД.
                    userForwork.setId(User.getCount());
                    users.put(userForwork.getId(), userForwork);
                    log.info("Выполнено создание нового пользователя с логином = '" + userForwork.getLogin()
                            + "' и ID = '" + userForwork.getId() + "'.");
                    return ResponseEntity.ok(userForwork);
                } else if (users.containsKey(userForwork.getId())) {
                    //} иначе если ID пользователя есть в БД {
                    //          обновление логина пользователя -> обновляем запись в БД.
                    String oldLogin = users.get(userForwork.getId()).getLogin();
                    users.put(userForwork.getId(), userForwork);
                    log.info("Выполнено обновление учётной записи с изменением логина. " +
                            "Был логин = '" + oldLogin + "', а стал - '" + userForwork.getLogin() + "'.");
                    return ResponseEntity.ok(userForwork);
                } else {
                    //ID и Логина нет в БД. -> Не верный запрос.
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("В запросе на обновление пользователя ошибка. Login '" + userForwork.getLogin()
                                    + "'и ID = '" + userForwork.getId() + "' не найдены в БД.");
                }
            }
/*
        }
                //Получается обновление существующей записи о пользователе, но в запросе ошибка в ID.
                String login = users.get(idFromDB).getLogin();
                users.put(user.getId(), user);
                log.info("Выполнено обновление существующей записи о пользователе с логином: \""
                        + user.getLogin() + "\".");
                return ResponseEntity.ok(user);
            } else if (isUserIdAlreadyExistInDB(user) != null) {
                //Получается смена логина.
                String login = users.get(user.getId()).getLogin();
                System.out.println("Выполнено обновление учётной записи с изменением логина." +
                        "Был логин = " + login + ", а стал - " + user.getLogin());
                users.put(user.getId(), user);
            } else
                //Получается, что на входе вообще неизвестный юзер. Не совпадает ни ID, ни login.
            
            
*/
/*            users.put(user.getId(), user);
            log.info("Пользователь \"" + user.getLogin() + "\" успешно добавлен в базу данных.");*//*

//            return ResponseEntity.ok("Пользователь \"" + user.getLogin() + "\" успешно добавлен в базу данных.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
*/
        } catch (ValidateException ex) {
            log.error("Ошибка обновления пользователя в базе данных. Ошибка: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Ошибка добавления пользователя в базу данных. Ошибка: "
                    + ex.getMessage());
        }
    }
    
    
    /**
     * Для Film:
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
        String name = user.getName();
        LocalDate birthday = user.getBirthday();
        
        if (id == null) {
            log.info("checkUser(): ID пользователя = null.");
        }
        
        
        //электронная почта не может быть пустой и должна содержать символ @;
        if (email == null || email.isEmpty() || email.isBlank()) {
            log.info("checkUser(): Не пройдена проверка 'пустоты' адреса электронной почты.");
            throw new ValidateException("Адрес электронной почта не может быть пустым.");
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
        
        
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        //если имя отсутствует, то оно равно login:
        if (name == null || name.isEmpty() || name.isBlank()) {
            user.setName(login);
        }
        
        
        //дата рождения не может быть в будущем
        if (birthday != null) {
            if (birthday.isAfter(LocalDate.now())) {
                log.info("checkUser(): Не пройдена проверка корректной даты рождения. Дата рождения ещё не наступила");
                throw new ValidateException("Дата рождения ещё не наступила. Введите корректную дату рождения.");
            }
        }
    }
    
    /**
     * Метод проверки наличия пользователя в базе данных.
     *
     * @param user пользователь, наличие логина которого необходимо проверить в базе данных.
     * @return ID, найденный в БД по логину.
     * Если возвращается не null, то после этой проверки можно обновлять пользователя,
     * присвоив ему ID из базы данных.
     * <p>null - пользователя нет в базе данных.</p>
     */
    private Integer idUserLoginAlreadyExistInDB(User user) {
        String login = user.getLogin();
        for (User u : users.values()) {
            if (u.getLogin().equals(login)) {
                return u.getId();
            }
        }
        return null;
    }
    
    /**
     * Метод проверки наличия ID пользователя в базе данных.
     *
     * @param user пользователь, наличие ID которого необходимо проверить в базе данных.
     * @return возвращает логин пользователя, ID которого присутствует в базе данных.
     * <p>null - пользователя с ID нет в базе данных.</p>
     * <P>Если null, то не происходит смена login</P>
     */
    private String isUserIdAlreadyExistInDB(User user) {
        Integer id = user.getId();
        for (User u : users.values()) {
            if (u.getId().equals(id)) {
                return u.getLogin();
            }
        }
        return null;
    }
}
