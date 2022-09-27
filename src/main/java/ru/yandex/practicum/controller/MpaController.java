package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@Component

public class MpaController {
    
    @Qualifier("MpaDBService")
    private final MpaService mpaService;
    
    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }
    
    @GetMapping("/mpa")
    public List<Mpa> getAllMpa(){
        return mpaService.getAllMpa();
    }
    
    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable("id") Integer id){
        return mpaService.getMpaById(id);
    }
    
}
