package ru.yandex.practicum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

import java.util.List;

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
@Component
@RequiredArgsConstructor
public class UserController {
    UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    static final String PATH_FOR_USERS = "/users";
    private static final String PATH_FOR_ID_VARIABLE = "/{id}";
    private static final String PATH_FOR_FRIENDS = "/friends";
    private static final String PATH_FOR_FRIENDS_ID_VARIABLE = "/{friendId}";
    private static final String PATH_FOR_COMMON = "/common";
    private static final String PATH_FOR_OTHER_ID_VARIABLE = "/{otherId}";
    
    /**
     * Получение списка всех пользователей.
     */
//    @Override
    @GetMapping(PATH_FOR_USERS)
    public List<User> getAllUsers() {
        log.info("Выдан ответ на запрос всех пользователей.");
        return userService.getAllUsers();
    }
    
    /**
     * Создание пользователей.
     *
     * @param user из тела запроса.
     * @return созданный пользователь.
     */
    @PostMapping(PATH_FOR_USERS)
    public ResponseEntity<?> createUser(@Validated @RequestBody User user) {
        User createdUser = userService.addToStorage(user);
        log.info("Выдан ответ на запрос создания пользователя: {}", createdUser);
        return ResponseEntity.ok(createdUser);
    }
    
    
    /**
     * Обновление информации о существующем пользователе.
     *
     * @param user обновляемый пользователь.
     * @return ответ о совершённом действии.
     */
    @PutMapping(PATH_FOR_USERS)
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateInStorage(user);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Получение пользователя по ID.
     *
     * @param id ID пользователя.
     * @return null, если пользователь не найден в БД.
     */
    @GetMapping(PATH_FOR_USERS + PATH_FOR_ID_VARIABLE)
    public User getUser(@PathVariable Integer id) {
        log.info("Выдан ответ на запрос пользователя по ID = " + id + ".");
        return userService.getUserById(id);
    }
    
    
    /**
     * PUT /users/{id}/friends/{friendId} — добавление в друзья.
     *
     * @param id       ID инициатора дружбы.
     * @param friendId ID будущего друга.
     * @return Запрос на дружбу с пользователем (ID = friendId) успешно обработан.
     */
    @PutMapping(PATH_FOR_USERS + PATH_FOR_ID_VARIABLE + PATH_FOR_FRIENDS + PATH_FOR_FRIENDS_ID_VARIABLE)
    public ResponseEntity<?> addEachOtherAsFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addEachOtherAsFriends(id, friendId);
        log.info("Пользователь (ID = " + id + ") подружился с пользователем (ID = " + friendId + ").");
        return ResponseEntity.status(HttpStatus.OK).body("Запрос на дружбу с пользователем (ID = "
                + friendId + ") успешно обработан.");
    }
    
    
    /**
     * DELETE /users/{id}/friends/{friendId} — удаление из друзей.
     *
     * @param id       ID инициатора удаления из друзей.
     * @param friendId ID бывшего друга.
     * @return Запрос на завершение дружбы с пользователем (ID = friendId) успешно обработан.
     */
    @DeleteMapping(PATH_FOR_USERS + PATH_FOR_ID_VARIABLE + PATH_FOR_FRIENDS + PATH_FOR_FRIENDS_ID_VARIABLE)
    public ResponseEntity<?> deleteFromFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFromFriends(id, friendId);
        log.info("Грусть. Дружба пользователя (ID = " + id + ") с пользователем (ID = " + friendId + ") завершена )-;");
        return ResponseEntity.status(HttpStatus.OK).body("Запрос на завершение дружбы с пользователем (ID = "
                + friendId + ") успешно обработан.");
    }
    
    /**
     * GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
     * @param id ID пользователя, для которого необходимо найти друзей.
     * @return список друзей.
     */
    @GetMapping(PATH_FOR_USERS + PATH_FOR_ID_VARIABLE + PATH_FOR_FRIENDS)
    public List<User> getUserFriends(@PathVariable Integer id) {
        List<User> result = userService.getUserFriends(id);
        log.info("Выдан ответ на запрос информации о друзьях пользователя с ID = " + id);
        return result;
    }
    
    /**
     * GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
     * @param id ID пользователя №1.
     * @param otherId ID пользователя №2.
     * @return список общих друзей.
     */
    @GetMapping(PATH_FOR_USERS + PATH_FOR_ID_VARIABLE + PATH_FOR_FRIENDS + PATH_FOR_COMMON
            + PATH_FOR_OTHER_ID_VARIABLE)
    public List<User> getCommonFriends (@PathVariable Integer id, @PathVariable Integer otherId) {
        List<User> result = userService.getCommonFriends(id, otherId);
        log.info("Выдан ответ на запрос информации об общих друзьях пользователя с ID = " + otherId);
        return result;
    }
}
