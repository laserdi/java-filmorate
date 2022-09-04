package ru.yandex.practicum.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;

@RestControllerAdvice
@Slf4j
@Getter
public class ErrorHandler {
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleForNotFound(final NotFoundRecordInBD ex) {
        String error = "Error 404. Not Found.";
        String message = ex.getMessage();
        log.error(error + " — " + message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error + " — " + message);
//        return ResponseEntity.notFound().build();//body(error + message);
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleForBadRequest(final ValidateException ex) {
        String error = "Error 400. Bad Request.";
        String message = ex.getMessage();
        log.error(error + " — " + message);
        return ResponseEntity.badRequest().body(error + " — " + message);
    }
}
