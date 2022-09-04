package ru.yandex.practicum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>электронная почта не может быть пустой и должна содержать символ @;</p>
 * <p>логин не может быть пустым и содержать пробелы;</p>
 * <p>имя для отображения может быть пустым — в таком случае будет использован логин;</p>
 * <p>дата рождения не может быть в будущем.</p>
 */
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

//    User userIsCorrectly;
//    List<User> expectedUserList;
    
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    UserService userService;
    
    @MockBean
    private UserStorage userStorage;
    
//    @MockBean
//    UserController userController;
    
    User userIsCorrectly = User.builder().id(0).email("email@email").login("login").name("name")
            .birthday(LocalDate.of(2000, 1, 3)).build();
    
    
/*
    @BeforeEach
    void setup() {
        userIsCorrectly = User.builder().id(0).email("email@email").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 3)).build();
        
//        UserStorage userStorage = new InMemoryUserStorage();
//        userService = new UserService(userStorage);
//        userController = new UserController(userService);
        expectedUserList = new ArrayList<>();
        
    }
*/
    
    
    //электронная почта не может быть пустой и должна содержать символ @;
    @Test
    public void createdUserWithFailEmail() throws Exception {
//
        User userEmailIsNull = userIsCorrectly.toBuilder().email(null).build();
        User userEmailIsEmpty = userIsCorrectly.toBuilder().id(1).email("").build();
        User userEmailIsBlank = userIsCorrectly.toBuilder().id(2).email(" ").build();
        User userEmailWithoutSobaka = userIsCorrectly.toBuilder().id(3).email("email.email").build();
        
        //assertDoesNotThrow(()->userController.createUser(userIsCorrectly));
        //System.out.println(userController.createUser(userIsCorrectly));
        
/*
        assertEquals(HttpStatus.OK.toString(), userController.createUser(userIsCorrectly).getStatusCode());
        assertThrows(ValidateException.class, ()->userController.createUser(userEmailIsNull));
        assertThrows(ValidateException.class, ()->userController.createUser(userEmailIsEmpty));
        assertThrows(ValidateException.class, ()->userController.createUser(userEmailIsBlank));
        assertThrows(ValidateException.class, ()->userController.createUser(userEmailWithoutSobaka));
*/
    
        System.out.println(objectMapper.writeValueAsString(userEmailIsNull));
        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/Json")
                        .content(objectMapper.writeValueAsString(userEmailIsNull)))
                .andExpect(status().isBadRequest())
                .andReturn();
        //assertEquals(400, mvcResult.getResponse().getStatus());
        
        
/*
        assertDoesNotThrow(() -> userController.createUser(userIsCorrectly)
                , "Ошибка тестирования при корректном 'email'.");
        System.out.println(userIsCorrectly);
        //assertEquals();
        assertThrows(ValidateException.class, () -> userController.createUser(userEmailIsNull)
                , "Ошибка тестирования при 'email' = null.");
        assertThrows(ValidateException.class, () -> userController.createUser(userEmailIsEmpty)
                , "Ошибка тестирования при пустом значении 'email'.");
        assertThrows(ValidateException.class, () -> userController.createUser(userEmailIsBlank)
                , "Ошибка тестирования при 'email', состоящем только из пробелов.");
        assertThrows(ValidateException.class, () -> userController.createUser(userEmailWithoutSobaka)
                , "Ошибка тестирования при 'email', не содержащем символ '@'.");
*/
    }
    
    //логин не может быть пустым и содержать пробелы;
    @Test
    public void createdUserWithFailLogin() {
        User userLoginIsNull = userIsCorrectly.toBuilder().login(null).build();
        User userLoginIsEmpty = userIsCorrectly.toBuilder().login("").build();
        User userLoginIsBlank = userIsCorrectly.toBuilder().login(" ").build();
        
/*
        assertDoesNotThrow(() -> userController.createUser(userIsCorrectly)
                , "Ошибка тестирования при корректном 'login'.");
        
        assertThrows(ValidateException.class, () -> userController.createUser(userLoginIsNull)
                , "Ошибка тестирования при 'login' = null.");
        assertThrows(ValidateException.class, () -> userController.createUser(userLoginIsEmpty)
                , "Ошибка тестирования при пустом 'login'.");
        assertThrows(ValidateException.class, () -> userController.createUser(userLoginIsBlank)
                , "Ошибка тестирования при 'login', состоящем только из пробелов.");
*/
    }
    
    //имя для отображения может быть пустым — в таком случае будет использован логин;
    @Test
    public void createdUserWithEmptyName() {
        User userIsCorrectlyAfterChecking = userIsCorrectly.toBuilder().build();
//        userController.createUser(userIsCorrectlyAfterChecking);
        
        User userNameIsNull = userIsCorrectly.toBuilder().name(null).build();
        User userNameIsNullAfterChecking = userNameIsNull.toBuilder().build();
//        userController.createUser(userNameIsNullAfterChecking);
        
        User userNameIsEmpty = userIsCorrectly.toBuilder().name("").build();
        User userNameIsEmptyAfterChecking = userNameIsEmpty.toBuilder().name("").build();
//        userController.createUser(userNameIsEmptyAfterChecking);
        
        User userNameIsBlank = userIsCorrectly.toBuilder().name(" ").build();
        User userNameIsBlankAfterChecking = userNameIsBlank.toBuilder().build();
//        userController.createUser(userNameIsBlankAfterChecking);
        
        //Тест правильно заполненного юзера:
/*
        assertEquals(
                "User(id=0, email=email@email, login=login, name=name, birthday=2000-01-03)"
                , userIsCorrectly.toString()
                , "Ошибка тестирования перевода в строку полностью заполненного юзера.");
        assertEquals(userIsCorrectlyAfterChecking, userIsCorrectly,
                "Ошибка тестирования сравнения полностью заполненного юзера и его же но после проверки.");
        
        //Тест юзера с name = null:
        assertEquals(
                "User(id=0, email=email@email, login=login, name=login, birthday=2000-01-03)"
                , userNameIsNullAfterChecking.toString()
                , "Ошибка тестирования перевода в строку юзера с 'name' = null.");
        
        //Тест юзера с пустым name:
        assertEquals(
                "User(id=0, email=email@email, login=login, name=login, birthday=2000-01-03)"
                , userNameIsEmptyAfterChecking.toString()
                , "Ошибка тестирования перевода в строку юзера с пустым значением 'name'.");
        
        //Тест юзера с name, состоящим только из пробелов:
        assertEquals(
                "User(id=0, email=email@email, login=login, name=login, birthday=2000-01-03)"
                , userNameIsBlankAfterChecking.toString()
                , "Ошибка тестирования перевода в строку юзера со значением 'name'," +
                        " состоящим только из пробелов.");
*/
    }
    
    //дата рождения не может быть в будущем.
    @Test
    public void createdUserWithFailBirthday() {
        User userFromFuture = userIsCorrectly.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        User userFromToday = userIsCorrectly.toBuilder().birthday(LocalDate.now()).build();
        User userFromYesterday = userIsCorrectly.toBuilder().birthday(LocalDate.now().minusDays(1)).build();
        
/*
        assertDoesNotThrow(() -> userController.createUser(userIsCorrectly)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого 'в норме'.");
        
        assertThrows(ValidateException.class, () -> userController.createUser(userFromFuture)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого завтра.");
        
        assertDoesNotThrow(() -> userController.createUser(userFromYesterday)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого вчера.");
        
        assertDoesNotThrow(() -> userController.createUser(userFromToday)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого сегодня.");
*/
    
    
    }
}