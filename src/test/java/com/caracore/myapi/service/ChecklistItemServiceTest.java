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
import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.entities.ChecklistItemEntity;
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
    
}
