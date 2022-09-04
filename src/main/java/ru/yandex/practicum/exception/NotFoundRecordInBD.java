package ru.yandex.practicum.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundRecordInBD extends RuntimeException{
    
    public NotFoundRecordInBD(String message) {
        super(message);
    }
}
