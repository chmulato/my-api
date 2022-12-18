package com.caracore.myapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.caracore.myapi.entities.ChecklistItemEntity;

public interface ChecklistItemRepository extends PagingAndSortingRepository<ChecklistItemEntity, Long> {
    
    Optional<ChecklistItemEntity> findByGuid(String guid);

    List<ChecklistItemEntity> findByDescriptionAndIsCompleted(String description, Boolean isCompleted);
    
}
