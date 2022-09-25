package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.storage.friends.dao.FriendsStorage;

@Service
@Slf4j
public class FriendsService {
    
    @Qualifier("FriendsDBStorage")
    private final FriendsStorage friendsDBStorage;
    
    
    public FriendsService(FriendsStorage friendsDBStorage) {
        this.friendsDBStorage = friendsDBStorage;
    }
}
