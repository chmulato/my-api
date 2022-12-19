package com.caracore.myapi.dto;

import java.time.LocalDate;

import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.entities.ChecklistItemEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChecklistItemDTO {

    private String guid;

    @NotBlank(message = "Checklist item description cannot be either null or empty")
    private String description;

    @NotNull(message = "Is completed is mandatory")
    private Boolean isCompleted;

    @NotNull(message = "Deadline is mandatory")
    private LocalDate deadline;

    @NotBlank(message = "Category cannot be either null or empty")
    private CategoryEntity Category;

    public static ChecklistItemDTO toDTO(ChecklistItemEntity checklistItemEntity) {
        return ChecklistItemDTO.builder()
            .guid(checklistItemEntity.getGuid())
            .description(checklistItemEntity.getDescription())
            .deadline(checklistItemEntity.getDeadline())
            .isCompleted(checklistItemEntity.getIsCompleted())
            .Category(checklistItemEntity.getCategory())
            .build();
    }

}
