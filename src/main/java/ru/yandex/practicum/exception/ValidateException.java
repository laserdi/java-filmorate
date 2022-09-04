package ru.yandex.practicum.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidateException extends RuntimeException {
    
    public ValidateException(String message) {
        super(message);
    }
}
