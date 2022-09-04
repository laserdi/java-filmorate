package ru.yandex.practicum.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.exception.NotFoundRecordInBD;
import ru.yandex.practicum.exception.ValidateException;
import ru.yandex.practicum.model.ErrorResponse;

@RestController
@Slf4j
@Getter
public class ErrorHandler {
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleForNotFound(final NotFoundRecordInBD ex) {
        String error = "*****************HttpStatus.NOT_FOUND" + "\t" + ex.getMessage();
        log.error(error);
        return new ErrorResponse(error);
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleForBadRequest(final ValidateException ex) {
        String error = "********************HttpStatus.BAD_REQUEST" + "\t" + ex.getMessage();
        log.error(error);
        return new ErrorResponse(error);
    }
}
