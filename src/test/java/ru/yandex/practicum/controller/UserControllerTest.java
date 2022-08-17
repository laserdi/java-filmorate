package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.ValidateException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>электронная почта не может быть пустой и должна содержать символ @;</p>
 * <p>логин не может быть пустым и содержать пробелы;</p>
 * <p>имя для отображения может быть пустым — в таком случае будет использован логин;</p>
 * <p>дата рождения не может быть в будущем.</p>
 */
class UserControllerTest {
    UserController userController = new UserController();
    User userIsCorrectly = User.builder().id(0).email("email@email").login("login").name("name")
            .birthday(LocalDate.of(2000, 1, 3)).build();
    
    //электронная почта не может быть пустой и должна содержать символ @;
    @Test
    public void createdUserWithFailEmail() {
        User userEmailIsNull = userIsCorrectly.toBuilder().email(null).build();
        User userEmailIsEmpty = userIsCorrectly.toBuilder().email("").build();
        User userEmailIsBlank = userIsCorrectly.toBuilder().email(" ").build();
        User userEmailWithoutSobaka = userIsCorrectly.toBuilder().email("email.email").build();
        
        assertDoesNotThrow(() -> userController.checkUser(userIsCorrectly)
                , "Ошибка тестирования при корректном 'email'.");
        
        assertThrows(ValidateException.class, () -> userController.checkUser(userEmailIsNull)
                , "Ошибка тестирования при 'email' = null.");
        assertThrows(ValidateException.class, () -> userController.checkUser(userEmailIsEmpty)
                , "Ошибка тестирования при пустом значении 'email'.");
        assertThrows(ValidateException.class, () -> userController.checkUser(userEmailIsBlank)
                , "Ошибка тестирования при 'email', состоящем только из пробелов.");
        assertThrows(ValidateException.class, () -> userController.checkUser(userEmailWithoutSobaka)
                , "Ошибка тестирования при 'email', не содержащем символ '@'.");
    }
    
    //логин не может быть пустым и содержать пробелы;
    @Test
    public void createdUserWithFailLogin() {
        User userLoginIsNull = userIsCorrectly.toBuilder().login(null).build();
        User userLoginIsEmpty = userIsCorrectly.toBuilder().login("").build();
        User userLoginIsBlank = userIsCorrectly.toBuilder().login(" ").build();
        
        assertDoesNotThrow(()->userController.checkUser(userIsCorrectly)
                ,"Ошибка тестирования при корректном 'login'.");
        
        assertThrows(ValidateException.class, ()->userController.checkUser(userLoginIsNull)
                , "Ошибка тестирования при 'login' = null.");
        assertThrows(ValidateException.class, ()-> userController.checkUser(userLoginIsEmpty)
                , "Ошибка тестирования при пустом 'login'.");
        assertThrows(ValidateException.class, ()-> userController.checkUser(userLoginIsBlank)
                , "Ошибка тестирования при 'login', состоящем только из пробелов.");
    }
    
    //имя для отображения может быть пустым — в таком случае будет использован логин;
    @Test
    public void createdUserWithEmptyName() {
        User userNameIsNull = userIsCorrectly.toBuilder().name(null).build();
        User userNameIsEmpty = userIsCorrectly.toBuilder().name("").build();
        User userNameIsBlank = userIsCorrectly.toBuilder().name(" ").build();
        
        assertEquals(
                "User{id=0, email='email@email', login='login', name='name', birthday=2000-01-03}"
                , userIsCorrectly.toString()
                , "Ошибка тестирования перевода в строку полностью заполненного юзера.");
        assertEquals(
                "User{id=0, email='email@email', login='login', name='login', birthday=2000-01-03}"
                , userNameIsNull.toString()
                , "Ошибка тестирования перевода в строку юзера с 'name' = null.");
        assertEquals(
                "User{id=0, email='email@email', login='login', name='login', birthday=2000-01-03}"
                , userNameIsEmpty.toString()
                , "Ошибка тестирования перевода в строку юзера с пустым значением 'name'.");
        assertEquals(
                "User{id=0, email='email@email', login='login', name='login', birthday=2000-01-03}"
                , userNameIsBlank.toString()
                , "Ошибка тестирования перевода в строку юзера со значением 'name'," +
                        " состоящим только из пробелов.");
    }
    
    //дата рождения не может быть в будущем.
    @Test
    public void createdUserWithFailBirthday() {
        User userFromFuture =userIsCorrectly.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        User userFromToday = userIsCorrectly.toBuilder().birthday(LocalDate.now()).build();
        User userFromYesterday = userIsCorrectly.toBuilder().birthday(LocalDate.now().minusDays(1)).build();
        
        assertDoesNotThrow(()->userController.checkUser(userIsCorrectly)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого 'в норме'.");
        
        assertThrows(ValidateException.class, ()->userController.checkUser(userFromFuture)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого завтра.");
        
        assertDoesNotThrow(()->userController.checkUser(userFromYesterday)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого вчера.");
        
        assertDoesNotThrow(()->userController.checkUser(userFromToday)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого сегодня.");
        
        
    }
}