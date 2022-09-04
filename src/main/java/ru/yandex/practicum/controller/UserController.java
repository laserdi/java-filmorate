package ru.yandex.practicum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

import javax.validation.Valid;
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
//    @Override
    @PostMapping(PATH_FOR_USERS)
    public ResponseEntity<?> createUser(@Validated @RequestBody User user) {
        return ResponseEntity.ok(userService.addToStorage(user));
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
}
