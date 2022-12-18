package com.caracore.myapi.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity extends BaseEntity {

    private Long categoryId;

    private String name;
    
    private List<ChecklistItemEntity> checklistItems;
    
}
