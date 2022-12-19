package com.caracore.myapi.dto;

import com.caracore.myapi.entities.CategoryEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryDTO {
    
    private String guid;
    
    private String name;

    public static CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .guid(categoryEntity.getGuid())
                .name(categoryEntity.getName())
                .build();
    }

}
