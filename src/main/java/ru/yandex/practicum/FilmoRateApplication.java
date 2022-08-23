package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmoRateApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(FilmoRateApplication.class, args);
        System.out.println("Сервер запущен.");
    }
    
}
