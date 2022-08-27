package ru.yandex.practicum.exception;

public class NotFoundRecordInBD extends RuntimeException{
    
    public NotFoundRecordInBD(String message) {
        super(message);
    }
}
