package com.caracore.myapi.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name="ChecklistItem")
@Table(indexes = { @Index(name = "IDX_GUID_CHK", columnList = "guid")})
public class ChecklistItemEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checklistitemId;

    private String description;

    private Boolean isCompleted;

    private LocalDate deadline;

    private LocalDate postedDate;

    @ManyToOne
    private CategoryEntity category;

}
