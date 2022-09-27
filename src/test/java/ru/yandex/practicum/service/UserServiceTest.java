/*package ru.yandex.practicum.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.storage.user.dao.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    User userIsCorrectly;
    UserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage);
    ValidationService validationService = new ValidationService();
    List<User> expectedUserList;
    
    @BeforeEach
    void setUp() {
        userIsCorrectly = User.builder().id(0).email("email@email").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 3)).build();

        expectedUserList = new ArrayList<>();
    }
    
    @AfterEach
    void tearDown() {
    }
    
    @Test
    void getAllUsers() {
        assertEquals(expectedUserList, userService.getAllUsers()
                , "Ошибка сравнения пустого списка юзеров.");
        
        expectedUserList.add(userIsCorrectly);
        userService.addToStorage(userIsCorrectly);
        assertEquals(expectedUserList, userService.getAllUsers()
                , "Ошибка сравнения списка юзеров после добавления туда нормального юзера.");
    }
    
    @Test
    void addToStorageWithFailEmail() {
        User userEmailIsNull = userIsCorrectly.toBuilder().email(null).build();
        User userEmailIsEmpty = userIsCorrectly.toBuilder().id(1).email("").build();
        User userEmailIsBlank = userIsCorrectly.toBuilder().id(2).email(" ").build();
        User userEmailWithoutSobaka = userIsCorrectly.toBuilder().id(3).email("email.email").build();
        
        
        assertDoesNotThrow(() -> userService.addToStorage(userIsCorrectly)
                , "Ошибка тестирования при корректном 'email'.");
        assertThrows(ValidateException.class, () -> userService.addToStorage(userEmailIsNull)
                , "Ошибка тестирования при 'email' = null.");
        assertThrows(ValidateException.class, () -> userService.addToStorage(userEmailIsEmpty)
                , "Ошибка тестирования при пустом значении 'email'.");
        assertThrows(ValidateException.class, () -> userService.addToStorage(userEmailIsBlank)
                , "Ошибка тестирования при 'email', состоящем только из пробелов.");
        assertThrows(ValidateException.class, () -> userService.addToStorage(userEmailWithoutSobaka)
                , "Ошибка тестирования при 'email', не содержащем символ '@'.");
        
        expectedUserList.add(userIsCorrectly);
        assertEquals(expectedUserList, userService.getAllUsers()
                , "Ошибка сравнения списка юзеров после добавления туда юзеров с 'хорошим' "
                        + "адресом электронной почты, и четырьмя плохими.");
        
    }
    
    @Test
    public void addToStorageUserWithFailLogin() {
        User userLoginIsNull = userIsCorrectly.toBuilder().login(null).build();
        User userLoginIsEmpty = userIsCorrectly.toBuilder().login("").build();
        User userLoginIsBlank = userIsCorrectly.toBuilder().login(" ").build();
        
        assertDoesNotThrow(() -> userService.addToStorage(userIsCorrectly)
                , "Ошибка тестирования при корректном 'login'.");
        
        assertThrows(ValidateException.class, () -> userService.addToStorage(userLoginIsNull)
                , "Ошибка тестирования при 'login' = null.");
        assertThrows(ValidateException.class, () -> userService.addToStorage(userLoginIsEmpty)
                , "Ошибка тестирования при пустом 'login'.");
        assertThrows(ValidateException.class, () -> userService.addToStorage(userLoginIsBlank)
                , "Ошибка тестирования при 'login', состоящем только из пробелов.");
        
        expectedUserList.add(userIsCorrectly);
        assertEquals(expectedUserList, userService.getAllUsers()
                , "Ошибка сравнения списка юзеров после добавления туда юзеров с 'хорошим' "
                        + "логином, и тремя плохими.");
    }
    
    @Test
    public void addToStorageUserWithFailName() {
        User userIsCorrectlyAfterChecking = userIsCorrectly.toBuilder().build();
        userService.addToStorage(userIsCorrectlyAfterChecking);
        
        User userNameIsNull = userIsCorrectly.toBuilder().id(1).login("login1").name(null).build();
        User userNameIsNullAfterChecking = userService.addToStorage(userNameIsNull);
        
        User userNameIsEmpty = userIsCorrectly.toBuilder().id(2).login("login2").name("").build();
        User userNameIsEmptyAfterChecking = userService.addToStorage(userNameIsEmpty);
        
        User userNameIsBlank = userIsCorrectly.toBuilder().login("login3").id(3).name(" ").build();
        User userNameIsBlankAfterChecking = userService.addToStorage(userNameIsBlank);
        
        //Тест правильно заполненного юзера:
        assertEquals(
                "User(id=0, email=email@email, login=login, name=name, birthday=2000-01-03, idsFriends=null)"
                , userIsCorrectly.toString()
                , "Ошибка тестирования перевода в строку полностью заполненного юзера.");
        assertEquals(userIsCorrectlyAfterChecking, userIsCorrectly,
                "Ошибка тестирования сравнения полностью заполненного юзера и его же, но после проверки.");
        
        //Тест юзера с name = null:
        assertEquals(
                "User(id=1, email=email@email, login=login1, name=login1, birthday=2000-01-03, idsFriends=null)"
                , userNameIsNullAfterChecking.toString()
                , "Ошибка тестирования перевода в строку юзера с 'name' = null.");
        
        //Тест юзера с пустым name:
        assertEquals(
                "User(id=2, email=email@email, login=login2, name=login2, birthday=2000-01-03, idsFriends=null)"
                , userNameIsEmptyAfterChecking.toString()
                , "Ошибка тестирования перевода в строку юзера с пустым значением 'name'.");
        
        //Тест юзера с name, состоящим только из пробелов:
        assertEquals(
                "User(id=3, email=email@email, login=login3, name=login3, birthday=2000-01-03, idsFriends=null)"
                , userNameIsBlankAfterChecking.toString()
                , "Ошибка тестирования перевода в строку юзера со значением 'name'," +
                        " состоящим только из пробелов.");
    }
    
    @Test
    public void addToStorageUserWithFailBirthday() {
        User userFromFuture = userIsCorrectly.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        User userFromToday = userIsCorrectly.toBuilder().birthday(LocalDate.now()).build();
        User userFromYesterday = userIsCorrectly.toBuilder().birthday(LocalDate.now().minusDays(1)).build();
        
        assertDoesNotThrow(() -> userService.addToStorage(userIsCorrectly)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого 'в норме'.");
        
        assertThrows(ValidateException.class, () -> userService.addToStorage(userFromFuture)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого завтра.");
        
        assertDoesNotThrow(() -> userService.addToStorage(userFromYesterday)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого вчера.");
        
        assertDoesNotThrow(() -> userService.addToStorage(userFromToday)
                , "Ошибка тестирования проверки корректности пользователя, " +
                        "полная дата рождения которого сегодня.");
    }
    
    
    @Test
    void updateInStorageWithFailEmail() {
        User userEmailIsNull = userIsCorrectly.toBuilder().email(null).build();
        User userEmailIsEmpty = userIsCorrectly.toBuilder().email("").build();
        User userEmailIsBlank = userIsCorrectly.toBuilder().email(" ").build();
        User userEmailWithoutSobaka = userIsCorrectly.toBuilder().email("email.email").build();
        userService.addToStorage(userIsCorrectly);
        
        assertDoesNotThrow(() -> userService.updateInStorage(userIsCorrectly)
                , "Ошибка тестирования обновления юзера при корректном 'email'.");
        assertThrows(ValidateException.class, () -> userService.updateInStorage(userEmailIsNull)
                , "Ошибка тестирования обновления юзера при 'email' = null.");
        assertThrows(ValidateException.class, () -> userService.updateInStorage(userEmailIsEmpty)
                , "Ошибка тестирования обновления юзера при пустом значении 'email'.");
        assertThrows(ValidateException.class, () -> userService.updateInStorage(userEmailIsBlank)
                , "Ошибка тестирования обновления юзера при 'email', состоящем только из пробелов.");
        assertThrows(ValidateException.class, () -> userService.updateInStorage(userEmailWithoutSobaka)
                , "Ошибка тестирования обновления юзера при 'email', не содержащем символ '@'.");
        
        expectedUserList.add(userIsCorrectly);
        myAssertEqualsList(expectedUserList, userService.getAllUsers()
                , "Ошибка сравнения списка юзеров после добавления туда юзеров с 'хорошим' "
                        + "адресом электронной почты, и четырьмя плохими.");
        
    }
    
    @Test
    public void updateInStorageUserWithFailLogin() {
        User userLoginIsNull = userIsCorrectly.toBuilder().login(null).build();
        User userLoginIsEmpty = userIsCorrectly.toBuilder().login("").build();
        User userLoginIsBlank = userIsCorrectly.toBuilder().login(" ").build();
        
        userService.addToStorage(userIsCorrectly);
        assertDoesNotThrow(() -> userService.updateInStorage(userIsCorrectly)
                , "Ошибка тестирования обновления юзера при корректном 'login'.");
        
        assertThrows(ValidateException.class, () -> userService.updateInStorage(userLoginIsNull)
                , "Ошибка тестирования обновления юзера при 'login' = null.");
        assertThrows(ValidateException.class, () -> userService.updateInStorage(userLoginIsEmpty)
                , "Ошибка тестирования обновления юзера при пустом 'login'.");
        assertThrows(ValidateException.class, () -> userService.updateInStorage(userLoginIsBlank)
                , "Ошибка тестирования обновления юзера при 'login', состоящем только из пробелов.");
        
        expectedUserList.add(userIsCorrectly);
        myAssertEqualsList(expectedUserList, userService.getAllUsers()
                , "Ошибка сравнения списка юзеров после добавления туда юзеров с 'хорошим' "
                        + "логином, и тремя плохими.");
    }
    
    @Test
    public void updateInStorageUserWithEmptyName() {
        userService.addToStorage(userIsCorrectly);
        User userIsCorrectlyUpdatedNameAndBirthday = userIsCorrectly.toBuilder().name("new name")
                .birthday(LocalDate.of(1991, 8, 19)).build();
        User userIsCorrectlyAfterChecking = userService.updateInStorage(userIsCorrectly);
        
        User userNameIsNull = userIsCorrectly.toBuilder().name(null).build();
        
        User userNameIsEmpty = userIsCorrectly.toBuilder().name("").build();
        
        User userNameIsBlank = userIsCorrectly.toBuilder().name(" ").build();
        
        //Тест правильно заполненного юзера:
        assertEquals(
                userIsCorrectlyUpdatedNameAndBirthday.toString()
                , userService.updateInStorage(userIsCorrectlyUpdatedNameAndBirthday).toString()
                , "Ошибка тестирования обновления юзера и перевода в строку полностью заполненного юзера.");
        assertDoesNotThrow(() -> userService.updateInStorage(userIsCorrectly));
        
        assertEquals(userIsCorrectlyAfterChecking, userIsCorrectly,
                "Ошибка тестирования обновления юзера и сравнения полностью заполненного юзера и его же, " +
                        "но после обновления.");
        
        //Тест юзера с name = null:
        assertDoesNotThrow(() -> userService.updateInStorage(userNameIsNull)
                , "Ошибка тестирования обновления юзера с 'name' = null.");
        assertEquals("User(id=0, email=email@email, login=login, name=login, " +
                        "birthday=2000-01-03, idsFriends=null)"
                , userService.updateInStorage(userNameIsNull).toString()
                , "Ошибка тестирования обновления юзера с 'name' = null.");
        
        //Тест юзера с пустым name:
        assertDoesNotThrow(() -> userService.updateInStorage(userNameIsEmpty)
                , "Ошибка тестирования обновления юзера с пустым значением 'name'.");
        assertEquals("User(id=0, email=email@email, login=login, name=login, " +
                        "birthday=2000-01-03, idsFriends=null)"
                , userService.updateInStorage(userNameIsEmpty).toString()
                , "Ошибка тестирования обновления юзера с пустым значением 'name'.");
        
        //Тест юзера с name, состоящим только из пробелов:
        assertDoesNotThrow(() -> userService.updateInStorage(userNameIsBlank)
                , "Ошибка тестирования обновления юзера и перевода в строку юзера со значением 'name'," +
                        " состоящим только из пробелов.");
        assertEquals("User(id=0, email=email@email, login=login, name=login, " +
                        "birthday=2000-01-03, idsFriends=null)"
                , userService.updateInStorage(userNameIsBlank).toString()
                , "Ошибка тестирования обновления юзера и перевода в строку юзера со значением 'name'," +
                        " состоящим только из пробелов.");
    }
    
    @Test
    public void updateInStorageUserWithFailBirthday() {
        User userFromFuture = userIsCorrectly.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        User userFromToday = userIsCorrectly.toBuilder().birthday(LocalDate.now()).build();
        User userFromYesterday = userIsCorrectly.toBuilder().birthday(LocalDate.now().minusDays(1)).build();
        userService.addToStorage(userIsCorrectly);
        
        assertDoesNotThrow(() -> userService.updateInStorage(userIsCorrectly)
                , "Ошибка тестирования обновления юзера, полная дата рождения которого 'в норме'.");
        
        assertThrows(ValidateException.class, () -> userService.updateInStorage(userFromFuture)
                , "Ошибка тестирования обновления юзера, полная дата рождения которого завтра.");
        
        assertDoesNotThrow(() -> userService.updateInStorage(userFromYesterday)
                , "Ошибка тестирования обновления юзера, полная дата рождения которого вчера.");
        
        assertDoesNotThrow(() -> userService.updateInStorage(userFromToday)
                , "Ошибка тестирования обновления юзера, полная дата рождения которого сегодня.");
    }
    
    
    @Test
    void checkUser() {
        User userEmailIsNull = userIsCorrectly.toBuilder().email(null).build();
        User userEmailIsEmpty = userIsCorrectly.toBuilder().email("").build();
        User userEmailIsBlank = userIsCorrectly.toBuilder().email(" ").build();
        User userEmailWithoutSobaka = userIsCorrectly.toBuilder().email("email.email").build();
    
        User userLoginIsNull = userIsCorrectly.toBuilder().login(null).build();
        User userLoginIsEmpty = userIsCorrectly.toBuilder().login("").build();
        User userLoginIsBlank = userIsCorrectly.toBuilder().login(" ").build();
    
        User userNameIsNull = userIsCorrectly.toBuilder().name(null).build();
        User userNameIsEmpty = userIsCorrectly.toBuilder().name("").build();
        User userNameIsBlank = userIsCorrectly.toBuilder().name(" ").build();
    
        User userFromFuture = userIsCorrectly.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        User userFromToday = userIsCorrectly.toBuilder().birthday(LocalDate.now()).build();
        User userFromYesterday = userIsCorrectly.toBuilder().birthday(LocalDate.now().minusDays(1)).build();
    
        assertDoesNotThrow(()->userService.checkUser(userIsCorrectly)

                ,"Ошибка проверки правильно заполненного юзера.");
        assertThrows(ValidateException.class, () -> userService.checkUser(userEmailIsNull)
                , "Ошибка тестирования при 'email' = null.");
        assertThrows(ValidateException.class, () -> userService.checkUser(userEmailIsEmpty)
                , "Ошибка тестирования при пустом значении 'email'.");
        assertThrows(ValidateException.class, () -> userService.checkUser(userEmailIsBlank)
                , "Ошибка тестирования при 'email', состоящем только из пробелов.");
        assertThrows(ValidateException.class, () -> userService.checkUser(userEmailWithoutSobaka)
                , "Ошибка тестирования при 'email', не содержащем символ '@'.");
        
        assertThrows(ValidateException.class, ()->userService.checkUser(userLoginIsNull)
                , "Ошибка тестирования при 'login' = null.");
        assertThrows(ValidateException.class, ()->userService.checkUser(userLoginIsEmpty)
                , "Ошибка тестирования при пустом 'login'.");
        assertThrows(ValidateException.class, ()->userService.checkUser(userLoginIsBlank)
                , "Ошибка тестирования при 'login', состоящем только из пробелов.");

        assertDoesNotThrow(()->userService.checkUser(userNameIsNull)
        ,"Ошибка тестирования юзера с 'name' = null.");
        assertDoesNotThrow(()->userService.checkUser(userNameIsEmpty)
        ,"Ошибка тестирования юзера с пустым значением 'name'.");
        assertDoesNotThrow(()->userService.checkUser(userNameIsBlank)
        ,"Ошибка тестирования юзера со значением 'name', состоящим только из пробелов." );
        
    }
    
    @Test
    void addAndDeleteFromFriends() {
        User user1 = User.builder().id(1).login("login1").email("email1@email1").idsFriends(new HashSet<>()).build();
        User user2 = User.builder().id(2).login("login2").email("email2@email2").idsFriends(new HashSet<>()).build();
    
        userService.addToStorage(user1);
        userService.addToStorage(user2);
        
        //Добавление в друзья:
        userService.addEachOtherAsFriends(user1.getId(), user2.getId());
        //Проверяем первого друга.
        assertTrue(userService.getUserById(user1.getId()).getIdsFriends().contains(user2.getId())
                , "Ошибка тестирования добавления в друзья: Друг №1 не имеет в списке друзей друга №2.");
        assertEquals(1, userService.getUserById(user1.getId()).getIdsFriends().size()
                , "Ошибка тестирования добавления в друзья: Количество друзей друга №1 не равно одному.");
        //Проверяем второго друга.
        assertTrue(userService.getUserById(user2.getId()).getIdsFriends().contains(user1.getId())
                , "Ошибка тестирования добавления в друзья: Друг №2 не имеет в списке друзей друга №1.");
        assertEquals(1, userService.getUserById(user2.getId()).getIdsFriends().size()
                , "Ошибка тестирования добавления в друзья: Количество друзей друга №2 не равно одному.");
        
        //Удаление из друзей:
        userService.deleteFromFriends(user1.getId(), user2.getId());
        //Проверяем первого друга.
        assertFalse(userService.getUserById(user1.getId()).getIdsFriends().contains(user2.getId())
                , "Ошибка тестирования удаления из друзей: Друг №2 присутствует в списке друзей друга №1.");
        assertEquals(0, userService.getUserById(user1.getId()).getIdsFriends().size()
                , "Ошибка тестирования удаления из друзей: Количество друзей друга №1 не равно нулю.");
        //Проверяем второго друга.
        assertFalse(userService.getUserById(user2.getId()).getIdsFriends().contains(user1.getId())
                , "Ошибка тестирования удаления из друзей: Друг №1 присутствует в списке друзей друга №2.");
        assertEquals(0, userService.getUserById(user2.getId()).getIdsFriends().size()
                , "Ошибка тестирования удаления из друзей: Количество друзей друга №2 не равно нулю.");
    }
    
*/
    
    
    /**
     * Метод сравнения списков.
     * @param expectedList ожидаемый список.
     * @param actuallyList актуальный список.
     * @param message сообщение при ошибке сравнения списков.
     */
/*
    private void myAssertEqualsList(List<User> expectedList, List<User> actuallyList, String message) {
        List<User> differences = new ArrayList<>(expectedList);
        differences.removeAll(actuallyList);
        assertEquals(0, differences.size()
                , message);
    }
    
}
*/