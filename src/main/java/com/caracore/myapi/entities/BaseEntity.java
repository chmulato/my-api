package com.caracore.myapi.entities;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@MappedSuperclass
public class BaseEntity {

    private String guid;
    
}
