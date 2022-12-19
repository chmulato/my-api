package com.caracore.myapi.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caracore.myapi.dto.CategoryDTO;
import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.service.CategoryService;

import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {

        List<CategoryDTO> resp = StreamSupport.stream(this.categoryService
        .findAllCategories().spliterator(), false)
            .map(categoryEntity -> CategoryDTO.toDTO(categoryEntity))
            .collect(Collectors.toList());

        return new ResponseEntity<List<CategoryDTO>>(resp, HttpStatus.OK);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addnewCategory(@RequestBody CategoryDTO categoryDTO) {

        CategoryEntity newCategory = this.categoryService.addNewCategory(categoryDTO.getName());

        return new ResponseEntity<>(newCategory.getGuid(), HttpStatus.CREATED);   
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        
        if (!StringUtils.hasText(categoryDTO.getGuid())) {
            throw new ValidationException("Category guid cannot be empty or null");
        }

        this.categoryService.updateCategory(categoryDTO.getGuid(), categoryDTO.getName());
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCategory(@PathVariable String guid) {
        
        this.categoryService.deleteCategory(guid);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
