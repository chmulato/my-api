package com.caracore.myapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import com.caracore.myapi.dto.CategoryDTO;
import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.entities.ChecklistItemEntity;
import com.caracore.myapi.exception.ResourceNotFoundException;
import com.caracore.myapi.repository.CategoryRepository;
import com.caracore.myapi.repository.ChecklistItemRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MyApiApplication.class)
public class ChecklistItemServiceTest {
  
    
    private ChecklistItemService checklistItemService;

    @Mock
    private ChecklistItemRepository checklistItemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void initTest() {
        this.checklistItemService = new ChecklistItemService(categoryRepository,checklistItemRepository);
    }

    @Test
    public void shouldCreateChecklistItemSuccessfully() {

        //having
        String description = "Pessoal";
        Boolean isCompleted = Boolean.FALSE;
        LocalDate deadline = LocalDate.of(2022, 12, 12);
        String categoryGuid = UUID.randomUUID().toString();

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setGuid(categoryGuid);
        categoryEntity.setName("Pessoal");

        //when
        when(categoryRepository.findByGuid(categoryGuid)).thenReturn(Optional.of(categoryEntity));
        when(checklistItemRepository.save(any(ChecklistItemEntity.class))).thenReturn(new ChecklistItemEntity());

        ChecklistItemEntity checklistItemEntity = this.checklistItemService.addNewChecklistItem(description, isCompleted, deadline, categoryGuid);

        //then
        Assertions.assertNotNull(checklistItemEntity);
        verify(checklistItemRepository, times(1)).save(
            argThat(checklistItemEntityArg -> checklistItemEntityArg.getDescription().equals("Pessoal")
            && checklistItemEntityArg.getGuid() != null)
        );
    }

    @Test
    public void shouldThrownAnExceptionWhenCategoryIsEmptyOrNull() {

        //having
        String description = "Pessoal";
        Boolean isCompleted = Boolean.FALSE;
        LocalDate deadline = LocalDate.of(2022, 12, 12);
        String categoryGuid = UUID.randomUUID().toString();

        //when
        when(categoryRepository.findByGuid(categoryGuid)).thenReturn(Optional.empty());

        Exception exception =
            Assertions.assertThrows(ResourceNotFoundException.class, () -> this.checklistItemService.addNewChecklistItem(description, isCompleted, deadline, categoryGuid));

            Assertions.assertEquals("category not found", exception.getMessage());
    
    }

    @Test
    public void shouldUpdateChecklistItemSucessfully() {

        //having
        String guid = UUID.randomUUID().toString();
        String description = anyString();
        Boolean isCompleted = Boolean.TRUE;
        LocalDate deadline = LocalDate.of(2022, 12, 12);

        String guidCategory = UUID.randomUUID().toString();
        String name = "Pessoal";

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setGuid(guidCategory);
        categoryEntity.setName(name);

        CategoryDTO categoryDTO = CategoryDTO.toDTO(categoryEntity);

        ChecklistItemEntity savedChecklistItem = new ChecklistItemEntity();
        savedChecklistItem.setGuid(guid);
        savedChecklistItem.setDescription(description);
        savedChecklistItem.setIsCompleted(isCompleted);
        savedChecklistItem.setDeadline(deadline);
        savedChecklistItem.setCategory(categoryEntity);

        ChecklistItemEntity retrievedChecklistItem = new ChecklistItemEntity();
        retrievedChecklistItem.setGuid(guid);
        retrievedChecklistItem.setDescription(description);
        retrievedChecklistItem.setIsCompleted(!isCompleted);
        retrievedChecklistItem.setDeadline(deadline);
        savedChecklistItem.setCategory(categoryEntity);

        //when
        when(checklistItemRepository.findByGuid(guid)).thenReturn(Optional.of(retrievedChecklistItem));
        when(categoryRepository.findByGuid(guidCategory)).thenReturn(Optional.of(categoryEntity));
        when(checklistItemRepository.save(savedChecklistItem)).thenReturn(retrievedChecklistItem);

        ChecklistItemEntity checklistItemEntity = this.checklistItemService.updateChecklistItem(guid, description, isCompleted, deadline, categoryDTO);

        //then
        Assertions.assertTrue(checklistItemEntity.getIsCompleted());
        verify(checklistItemRepository, times(1)).save(
            argThat(checklistItemEntityArg -> checklistItemEntityArg.getIsCompleted().equals(Boolean.TRUE)
            && checklistItemEntityArg.getGuid().equals(guid))
        );
        
    }

    @Test
    public void shouldThrownAnExceptionWhenTryToUpdateAndGuidOfChecklistItemIsEmptyOrNull() {

        //having
        String guid = null;
        String description = "Teste";
        Boolean isCompleted = Boolean.TRUE;
        LocalDate deadline = LocalDate.of(2022, 12, 12);

        String guidCategory = UUID.randomUUID().toString();
        String name = "Pessoal";

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setGuid(guidCategory);
        categoryEntity.setName(name);

        CategoryDTO categoryDTO = CategoryDTO.toDTO(categoryEntity);

        //then
        Exception exception =
            Assertions.assertThrows(IllegalArgumentException.class, () -> 
            this.checklistItemService.updateChecklistItem(guid, description, isCompleted, deadline, categoryDTO));

            Assertions.assertEquals("Guid cannot be empty or null", exception.getMessage());
    
    }
    
    @Test
    public void shouldThrownAnExceptionWhenTryToUpdateAndChecklistItemAndItDoesNotExist() {

        //having
        String guid = UUID.randomUUID().toString();
        String description = anyString();
        Boolean isCompleted = Boolean.TRUE;
        LocalDate deadline = LocalDate.of(2022, 12, 12);

        String guidCategory = UUID.randomUUID().toString();
        String name = "Pessoal";

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(1L);
        categoryEntity.setGuid(guidCategory);
        categoryEntity.setName(name);

        CategoryDTO categoryDTO = CategoryDTO.toDTO(categoryEntity);

        Optional<ChecklistItemEntity> retrievedChecklistItem = null;

        //when
        when(checklistItemRepository.findByGuid(guid)).thenReturn(retrievedChecklistItem);

        //then
        Exception exception =
            Assertions.assertThrows(ResourceNotFoundException.class, () -> 
            this.checklistItemService.updateChecklistItem(guid, description, isCompleted, deadline, categoryDTO));

            Assertions.assertEquals("Checklist Item not found", exception.getMessage());
    
    }

    
}
