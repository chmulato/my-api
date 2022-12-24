package com.caracore.myapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.caracore.myapi.MyApiApplication;
import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.exception.ResourceNotFoundException;
import com.caracore.myapi.repository.CategoryRepository;
import com.caracore.myapi.repository.ChecklistItemRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MyApiApplication.class)
public class CategoryServiceTest {
    
    
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ChecklistItemRepository checklistItemRepository;

    @BeforeEach
    public void initTest() {
        this.categoryService = new CategoryService(checklistItemRepository, categoryRepository);
    }

    @Test
    public void shouldCreateCategorySuccessfully() {

        //having
        String categoryName = "Personal";

        //when
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(new CategoryEntity());

        CategoryEntity categoryEntity = this.categoryService.addNewCategory(categoryName);

        //then
        Assertions.assertNotNull(categoryEntity);
        verify(categoryRepository, times(1)).save(
            argThat(categoryEntityArg -> categoryEntityArg.getName().equals("Personal")
            && categoryEntityArg.getGuid() != null)
        );
    }

    @Test
    public void shouldThrownAnExceptionWhenCategoryNameIsEmptyOrNull() {

        Exception exception =
            Assertions.assertThrows(IllegalArgumentException.class, () -> this.categoryService.addNewCategory(null));

            Assertions.assertEquals("Category name cannot be empty or null", exception.getMessage());
    
    }

    @Test
    public void shouldUpdateCategorySucessfully() {

        //having
        String guid = UUID.randomUUID().toString();
        String name = "Other";

        CategoryEntity savedCategory = new CategoryEntity();
        savedCategory.setGuid(guid);
        savedCategory.setName("Personal");

        //when
        when(categoryRepository.findByGuid(guid)).thenReturn(Optional.of(savedCategory));
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(new CategoryEntity());

        CategoryEntity categoryEntity = this.categoryService.updateCategory(guid, name);

        //then
        Assertions.assertNotNull(categoryEntity);
        verify(categoryRepository, times(1)).save(
            argThat(categoryEntityArg -> categoryEntityArg.getName().equals(name)
            && categoryEntityArg.getGuid().equals(guid))
        );
        
    }

    @Test
    public void shouldThrownAnExceptionWhenTryToUpdateAndCategoryGuidIsEmptyOrNull() {

        Exception exception =
            Assertions.assertThrows(IllegalArgumentException.class, () -> 
            this.categoryService.updateCategory("", "Sample name"));

            Assertions.assertEquals("Invalid parameters provided to update a category", exception.getMessage());
    
    }

    @Test
    public void shouldThrownAnExceptionWhenTryToUpdateAndCategoryAndNameIsEmptyOrNull() {

        Exception exception =
            Assertions.assertThrows(IllegalArgumentException.class, () -> 
            this.categoryService.updateCategory("any value", ""));

            Assertions.assertEquals("Invalid parameters provided to update a category", exception.getMessage());
    
    }

    @Test
    public void shouldThrownAnExceptionWhenTryToUpdateAndCategoryAndItDaesNotExist() {

        when(categoryRepository.findByGuid(anyString())).thenReturn(Optional.empty());

        Exception exception =
            Assertions.assertThrows(ResourceNotFoundException.class, () -> 
            this.categoryService.updateCategory("any value", "any name"));

            Assertions.assertEquals("Category not found.", exception.getMessage());
    
    }
}
