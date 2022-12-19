package com.caracore.myapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caracore.myapi.entities.ChecklistItemEntity;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItemEntity, Long> {
    
    Optional<ChecklistItemEntity> findByGuid(String guid);

    List<ChecklistItemEntity> findByDescriptionAndIsCompleted(String description, Boolean isCompleted);

    List<ChecklistItemEntity> findByCategoryGuid(String guid);
    
}
