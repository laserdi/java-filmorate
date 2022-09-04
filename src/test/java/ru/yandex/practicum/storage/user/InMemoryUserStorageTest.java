package ru.yandex.practicum.storage.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserStorageTest {
    UserStorage inMemoryUserStorage = new InMemoryUserStorage();
    User user1;
    User user2;
    
    @BeforeEach
    void setUp() {
        user1 = User.builder().id(1).login("login1").email("email1@email1").idsFriends(new HashSet<>()).build();
        user2 = User.builder().id(2).login("login2").email("email2@email2").idsFriends(new HashSet<>()).build();
    }
    
    @AfterEach
    void tearDown() {
    }
}