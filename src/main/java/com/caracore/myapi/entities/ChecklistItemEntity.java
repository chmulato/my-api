package com.caracore.myapi.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
