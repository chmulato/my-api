package com.caracore.myapi.dto;

import com.caracore.myapi.entities.CategoryEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryDTO {
    
    private String guid;
    
    @NotBlank(message = "Category name cannot be either null or empty")
    private String name;

    public static CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .guid(categoryEntity.getGuid())
                .name(categoryEntity.getName())
                .build();
    }

}
