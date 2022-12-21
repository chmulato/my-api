package com.caracore.myapi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.entities.ChecklistItemEntity;
import com.caracore.myapi.exception.ResourceNotFoundException;
import com.caracore.myapi.repository.CategoryRepository;
import com.caracore.myapi.repository.ChecklistItemRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChecklistItemService {

    private CategoryRepository categoryRepository;
    private ChecklistItemRepository checklistItemRepository;

    public ChecklistItemService(CategoryRepository categoryRepository, ChecklistItemRepository checklistItemRepository){
        this.categoryRepository = categoryRepository;        
        this.checklistItemRepository = checklistItemRepository;
    }

    private void validateChecklistItemData(String description, Boolean isCompleted, LocalDate deadline, String categoryGuid) {

        if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("Checklist Item must have a description");
        }

        if (isCompleted == null) {
            throw new IllegalArgumentException("Checklist Item must have a flag indicating if it is completed or not");
        }

        if (deadline == null) {
            throw new IllegalArgumentException("Checklist Item must have a deadline");
        }

        if (!StringUtils.hasText(categoryGuid)) {
            throw new IllegalArgumentException("In Checklist Item, category guid must be provided");
        }

    }

    public ChecklistItemEntity updateChecklistItem(String guid, String description, Boolean isCompleted, LocalDate deadline, String categoryGuid) {

        if (!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("Guid cannot be empty or null");
        }

        ChecklistItemEntity retrievedItem = this.checklistItemRepository.findByGuid(guid)
        .orElseThrow(() -> new ResourceNotFoundException("Checklist Item not found"));

        if (StringUtils.hasText(description)) {
            retrievedItem.setDescription(description);
        }

        if (isCompleted != null) {
            retrievedItem.setIsCompleted(isCompleted);
        }

        if (deadline != null) {
            retrievedItem.setDeadline(deadline);
        }

        if (!StringUtils.hasText(categoryGuid)) {

            CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(categoryGuid)
            .orElseThrow(() -> new ResourceNotFoundException("category not found"));

            retrievedItem.setCategory(retrievedCategory);
        }

        log.debug("Updating checklist item [ checklistItem = {}", retrievedItem.toString());

        return checklistItemRepository.save(retrievedItem);
    
    }

    public ChecklistItemEntity addNewChecklistItem(String description, Boolean isCompleted, LocalDate deadline, String categoryGuid){

        this.validateChecklistItemData(description, isCompleted, deadline, categoryGuid);

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(categoryGuid)
            .orElseThrow(() -> new ResourceNotFoundException("category not found"));

        ChecklistItemEntity checklistItemEntity = new ChecklistItemEntity();
        checklistItemEntity.setGuid(UUID.randomUUID().toString());
        checklistItemEntity.setIsCompleted(isCompleted);
        checklistItemEntity.setDescription(description);
        checklistItemEntity.setDeadline(deadline);
        checklistItemEntity.setPostedDate(LocalDate.now());
        checklistItemEntity.setCategory(retrievedCategory);

        log.debug("Adding new checklist item [ checklistItem = {} ]", checklistItemEntity.toString());

        return checklistItemRepository.save(checklistItemEntity);

    }

    public ChecklistItemEntity findChecklistItemByGuid(String guid) {
        if (!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("Guid of ChecklistItem cannot be empty or null");
        }

        return this.checklistItemRepository.findByGuid(guid).orElseThrow(
            () -> new ResourceNotFoundException("ChecklistItem not found.")
        );
    }

    public List<ChecklistItemEntity> findAllChecklistItems() {
        return checklistItemRepository.findAll();
    }
    
    public void deleteChecklistItem(String guid) {

        if (!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("Guid cannot be empty or null");
        }

        ChecklistItemEntity retrievedItem = this.checklistItemRepository.findByGuid(guid)
        .orElseThrow(() -> new ResourceNotFoundException("Checklist Item not found"));
    
        log.debug("Deleting checklist item [ guid = {}]", guid);

        this.checklistItemRepository.delete(retrievedItem);
    
    }

    public void updateIsCompleteStatus(String guid, boolean isComplete) {

        if (!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("Guid cannot be empty or null");
        }

        ChecklistItemEntity retrievedItem = this.checklistItemRepository.findByGuid(guid)
        .orElseThrow(() -> new ResourceNotFoundException("Checklist Item not found"));

        log.debug("Updating checklist item completed status [ guid = {}, isComplete = {} ]", guid, isComplete);

        retrievedItem.setIsCompleted(isComplete);

        this.checklistItemRepository.save(retrievedItem);
    
    }

}
