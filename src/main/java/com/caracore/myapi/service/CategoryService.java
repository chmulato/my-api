package com.caracore.myapi.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.entities.ChecklistItemEntity;
import com.caracore.myapi.exception.CategoryServiceException;
import com.caracore.myapi.exception.ResourceNotFoundException;
import com.caracore.myapi.repository.CategoryRepository;
import com.caracore.myapi.repository.ChecklistItemRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryService {

    private ChecklistItemRepository checklistItemRepository;
    private CategoryRepository categoryRepository;

    public CategoryService(ChecklistItemRepository checklistItemRepository, CategoryRepository categoryRepository){
        this.checklistItemRepository = checklistItemRepository;
        this.categoryRepository = categoryRepository;        
    }

    public CategoryEntity addNewCategory(String name) {

        if(!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Category name cannot be empty or null");
        }

        CategoryEntity newCategory = new CategoryEntity();
        newCategory.setGuid(UUID.randomUUID().toString());
        newCategory.setName(name);

        log.debug("Adding a new category with name [ name = {} ] ", name);

        return this.categoryRepository.save(newCategory);
    
    }

    public CategoryEntity updateCategory(String guid, String name) {

        if (guid == null || !StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Invalid parameters provided to update a category");
        }

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(guid).orElseThrow(
            () -> new ResourceNotFoundException("Category not found.")
        );

        retrievedCategory.setName(name);
        log.debug("Updating category [ guid = {}, newName = {} ]", guid, name);

        return this.categoryRepository.save(retrievedCategory);
    
    }

    public void deleteCategory(String guid) {
        if (!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("Category guid cannot be empty or null");
        }

        CategoryEntity retrievedCategory = this.categoryRepository.findByGuid(guid).orElseThrow(
            () -> new ResourceNotFoundException("Category not found.")
        );

        List<ChecklistItemEntity> checklistItems =  this.checklistItemRepository.findByCategoryGuid(guid);
        if (!CollectionUtils.isEmpty(checklistItems)) {
            throw new CategoryServiceException("It is not possible to delete given category as it has been used by checklist items");
        }

        log.debug("Deleting category [ guid = {} ]", guid);

        this.categoryRepository.delete(retrievedCategory);

    }

    public List<CategoryEntity> findAllCategories() {
        return this.categoryRepository.findAll();
    }

    public CategoryEntity findCategoryByGuid(String guid) {
        if (!StringUtils.hasText(guid)) {
            throw new IllegalArgumentException("Category guid cannot be empty or null");
        }

        return this.categoryRepository.findByGuid(guid).orElseThrow(
            () -> new ResourceNotFoundException("Category not found.")
        );
    }
    
}
