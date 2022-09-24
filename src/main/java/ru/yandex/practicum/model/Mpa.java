package ru.yandex.practicum.model;

import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Mpa {
    Integer id;
    String name;
    String description;
    
//    public String getName() {
//        return name;
//    }
    
//    public void setName(String name) {
//        this.name = name;
//    }
    
//    public String getDescription() {
//        return description;
//    }
    
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
