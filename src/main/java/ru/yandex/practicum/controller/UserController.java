package ru.yandex.practicum.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.ValidateException;


import java.time.LocalDate;
import java.util.HashMap;
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
    public Map<Integer, User> findAllUsers() {
/*
        User userForTest = User.builder()
                .id(-5)
                .email("edd@hyhhg")
                .login("login юзера №1")
                .name("имя юзера 1")
                .birthday(LocalDate.of(1983, 12, 20))
                .build();
*/
        //System.out.println(userForTest);
        //users.put(userForTest.getId(), userForTest);
        return users;
    }
    
    @PostMapping("/users")
    public ResponseEntity<String> createFilm(@RequestBody User user) {
        try {
            checkUser(user);
            
            if (isUserAlreadyExistInDB(user)) {
                //Получается обновление существующей записи о пользователе.
                users.put(user.getId(), user);
                return ResponseEntity.ok("Выполнено обновление существующей записи о пользователе: \""
                        + user.getLogin() + "\".");
            }
            
            users.put(user.getId(), user);
            return ResponseEntity.ok("Пользователь \"" + user.getLogin() + "\" успешно добавлен в базу данных.");
            
        } catch (ValidateException ex) {
            log.error("Ошибка добавления пользователя в базу данных. Ошибка: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Ошибка добавления пользователя в базу данных. Ошибка: "
                    + ex.getMessage());
        }
    }
    
    /**
     * Обновление информации о существующем пользователе.
     *
     * @param user обновляемый пользователь.
     * @return ответ о совершённом действии.
     */
    @PutMapping("/users")
    public ResponseEntity<String> updateFilm(@RequestBody User user) {
        try {
            checkUser(user);
            
            if (isUserAlreadyExistInDB(user)) {
                //Получается обновление существующей записи о пользователе.
                users.put(user.getId(), user);
                log.info("Выполнено обновление существующей записи о пользователе: \""
                        + user.getLogin() + "\".");
                return ResponseEntity.ok("Выполнено обновление существующей записи о пользователе: \""
                        + user.getLogin() + "\".");
            }
            
            users.put(user.getId(), user);
            log.info("Пользователь \"" + user.getLogin() + "\" успешно добавлен в базу данных.");
            return ResponseEntity.ok("Пользователь \"" + user.getLogin() + "\" успешно добавлен в базу данных.");
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
        
/*
        if (id == null) {
            log.info("Не пройдена проверка наличия ID пользователя. ID = null.");
            throw new ValidateException("Проверьте ID пользователя. ID отсутствует.");
        }
*/
        
        //электронная почта не может быть пустой и должна содержать символ @;
        if (email == null || email.isEmpty() || email.isBlank()) {
            log.info("Не пройдена проверка \"пустоты\" адреса электронной почты.");
            throw new ValidateException("Адрес электронной почта не может быть пустым.");
        }
        if (!email.contains("@")) {
            log.info("Не пройдена проверка наличия '@' в адресе электронной почты.");
            throw new ValidateException("Адрес электронной почты должен содержать символ '@'.");
        }
        
        
        //логин не может быть пустым и содержать пробелы;
        if (login == null || login.isEmpty() || login.isBlank()) {
            log.info("Не пройдена проверка наличия логина.");
            throw new ValidateException("Отсутствует логин пользователя.");
        }
        if (login.contains(" ")) {
            log.info("Не пройдена проверка отсутствия пробелов в логине.");
            throw new ValidateException("В логине не должно быть пробелов.");
        }
        
        
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        
        
        //дата рождения не может быть в будущем
        if (birthday != null) {
            if (birthday.isAfter(LocalDate.now())) {
                log.info("Не пройдена проверка корректной даты рождения. Дата рождения ещё не наступила");
                throw new ValidateException("Дата рождения ещё не наступила. Введите корректную дату рождения.");
            }
        }
    }
    
    /**
     * Метод проверки наличия пользователя в базу данных.
     *
     * @param user пользователь, наличие которого необходимо проверить в базе данных.
     * @return true - пользователь присутствует в базе данных.
     * <p>false - пользователя нет в базе данных.</p>
     */
    private boolean isUserAlreadyExistInDB(User user) {
        Integer id = user.getId();
        return users.containsKey(id);
    }
}
