package com.caracore.myapi.dto;

import java.time.LocalDate;

import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.entities.ChecklistItemEntity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChecklistItemDTO {

    private String guid;

    private String description;

    private Boolean isCompleted;

    private LocalDate deadline;

    private LocalDate postedDate;

    private CategoryEntity Category;

    public static ChecklistItemDTO toDTO(ChecklistItemEntity checklistItemEntity) {
        return ChecklistItemDTO.builder()
            .guid(checklistItemEntity.getGuid())
            .description(checklistItemEntity.getDescription())
            .deadline(checklistItemEntity.getDeadline())
            .postedDate(checklistItemEntity.getPostedDate())
            .isCompleted(checklistItemEntity.getIsCompleted())
            .Category(checklistItemEntity.getCategory())
            .build();
    }

}
