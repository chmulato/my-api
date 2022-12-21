package com.caracore.myapi.dto;

import java.time.LocalDate;

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

    private LocalDate postedDate;

    private CategoryDTO category;

    public static ChecklistItemDTO toDTO(ChecklistItemEntity checklistItemEntity) {
        return ChecklistItemDTO.builder()
            .guid(checklistItemEntity.getGuid())
            .description(checklistItemEntity.getDescription())
            .deadline(checklistItemEntity.getDeadline())
            .postedDate(checklistItemEntity.getPostedDate())
            .isCompleted(checklistItemEntity.getIsCompleted())
            .category(checklistItemEntity.getCategory() != null ? 
                CategoryDTO.builder()
                    .guid(checklistItemEntity.getCategory().getGuid())
                    .name(checklistItemEntity.getCategory().getName())
                .build() : null)
            .build();
    }

}
