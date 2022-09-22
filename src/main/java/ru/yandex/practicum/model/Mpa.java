package ru.yandex.practicum.model;

import lombok.*;

import javax.persistence.Entity;

//@Entity
@Builder(toBuilder = true)
@AllArgsConstructor//(access = AccessLevel .PUBLIC)
@NoArgsConstructor//(access = AccessLevel.PUBLIC)
@Setter//(value = AccessLevel.PUBLIC)
@Getter
public class Mpa {
    Integer id;
    String name;
    String description;
    
    public String getName() {
        return name;
    }
    
//    public void setName(String name) {
//        this.name = name;
//    }
    
    public String getDescription() {
        return description;
    }
    
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
