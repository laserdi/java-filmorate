package ru.yandex.practicum.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



//******************Добрый день, этот файл не готов.
//******************Его проверять не надо.

class UserControllerTest2 {
    UserController controller;
    User userIsCorrectly = User.builder().id(0).email("email@email").login("login").name("name")
            .birthday(LocalDate.of(2000, 1, 3)).build();
    @BeforeEach
    void setUp() {
/*
        ApplicationContext context = SpringApplication.run(UserControllerTest2.class);
        UserController controller = context.getBean(UserController.class);
*/
    }
    
    @AfterEach
    void tearDown() {
    }
    
    @Test
    void getAllUsers() {
    
    }
    
    @Test
    void createUser() {
        User userEmailIsNull = userIsCorrectly.toBuilder().email(null).build();
        User userEmailIsEmpty = userIsCorrectly.toBuilder().id(1).email("").build();
        User userEmailIsBlank = userIsCorrectly.toBuilder().id(2).email(" ").build();
        User userEmailWithoutSobaka = userIsCorrectly.toBuilder().id(3).email("email.email").build();
    
        List<User> expectedUserList = new ArrayList<>();
        assertThrows(ValidateException.class, ()->controller.createUser(userEmailIsNull));
    }
    
    @Test
    void updateUser() {
    }
}