package com.caracore.myapi.entities;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistItemEntity extends BaseEntity {
    
    private Long checklistitemId;

    private String description;

    private Boolean isCompleted;

    private LocalTime deadline;

    private LocalTime postedDate;

    private CategoryEntity category;

}
