package ru.yandex.practicum.model;

import ru.yandex.practicum.exception.NotFoundRecordInBD;


public enum FriendshipStatus {
    CONFIRMED,      //Подтверждена дружба
    UNCONFIRMED;    //Дружба не подтверждена
    
    public static FriendshipStatus getStatus(String str) {
        if (str.equals(CONFIRMED.name()))
            return CONFIRMED;
        else if (str.equals(UNCONFIRMED.name()))
            return UNCONFIRMED;
        else {
            throw new NotFoundRecordInBD("Такого значения ");
        }
    }
}
