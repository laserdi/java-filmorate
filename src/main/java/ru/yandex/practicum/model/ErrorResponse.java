package ru.yandex.practicum.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/*
 * Специальный объект для универсального формата ошибки.
 */
public class ErrorResponse {
    String error;
    String message;
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}


